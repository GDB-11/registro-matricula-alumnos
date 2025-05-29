package main;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ServiceContainer {
    private final Map<Class<?>, Supplier<?>> services = new HashMap<>();
    private final Map<Class<?>, Object> singletons = new HashMap<>();

    public <TInterface, TImplementation extends TInterface> void addTransient(
            Class<TInterface> interfaceType, Class<TImplementation> implementationType) {
                services.put(interfaceType, () -> {
                    try {
                        return implementationType.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Error creando instancia: " + implementationType.getName(), e);
                    }
        });
    }

    public <TInterface, TImplementation extends TInterface> void addSingleton(
            Class<TInterface> interfaceType, Class<TImplementation> implementationType) {
                services.put(interfaceType, () -> singletons.computeIfAbsent(interfaceType, k -> {
                    try {
                        return implementationType.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Error creando singleton de " + implementationType.getName(), e);
                    }
        }));
    }

    public <T> void addInstance(Class<T> type, T instance) {
        singletons.put(type, instance);
        services.put(type, () -> instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> type) {
        Supplier<?> supplier = services.get(type);
        if (supplier == null) {
            throw new IllegalArgumentException("Servicio no registrado para: " + type.getName());
        }
        return (T) supplier.get();
    }

    @SuppressWarnings("unchecked")
    public <T> T createWindow(Class<T> windowType) {
        try {
            Constructor<?>[] constructors = windowType.getConstructors();
            if (constructors.length == 0) {
                throw new RuntimeException("No existen contructures públicos para: " + windowType.getName());
            }

            Constructor<?> constructor = constructors[0];
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                parameters[i] = getService(parameterTypes[i]);
            }

            return (T) constructor.newInstance(parameters);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear ventana del tipo: " + windowType.getName(), e);
        }
    }
}
