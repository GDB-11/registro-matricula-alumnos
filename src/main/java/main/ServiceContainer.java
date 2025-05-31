package main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Dependency Injection Container para Java
 */
public class ServiceContainer {
    private final Map<Class<?>, Object> instances = new HashMap<>();
    private final Map<Class<?>, Class<?>> singletons = new HashMap<>();
    private final Map<Class<?>, Object> singletonInstances = new HashMap<>();

    /**
     * Guarda una instancia previamente creada
     */
    public <T> void addInstance(Class<T> serviceType, T instance) {
        instances.put(serviceType, instance);
    }

    /**
     * Crea un registro de tipo singleton
     */
    public <TService, TImplementation extends TService> void addSingleton(
            Class<TService> serviceType, 
            Class<TImplementation> implementationType) {
        singletons.put(serviceType, implementationType);
    }

    /**
     * Obtiene una intancia del servicio del contenedor
     */
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceType) {
        if (instances.containsKey(serviceType)) {
            return (T) instances.get(serviceType);
        }

        if (singletons.containsKey(serviceType)) {
            return getSingleton(serviceType);
        }

        throw new IllegalArgumentException("No se ha registrado el servicio: " + serviceType.getName());
    }

    @SuppressWarnings("unchecked")
    private <T> T getSingleton(Class<T> serviceType) {
        if (singletonInstances.containsKey(serviceType)) {
            return (T) singletonInstances.get(serviceType);
        }

        Class<?> implementationType = singletons.get(serviceType);
        T instance = createInstance((Class<T>) implementationType);
        singletonInstances.put(serviceType, instance);
        return instance;
    }

    @SuppressWarnings("unchecked")
    private <T> T createInstance(Class<T> type) {
        try {
            Constructor<?>[] constructors = type.getConstructors();

            if (constructors.length == 0) {
                throw new IllegalArgumentException("No existen constructores públicos para: " + type.getName());
            }

            Constructor<?> constructor = constructors[0]; //Obtiene el primer constructor
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                parameters[i] = getService(parameterTypes[i]);
            }

            return (T) constructor.newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | 
                 InvocationTargetException e) {
            throw new RuntimeException("Error creando instancia de: " + type.getName(), e);
        }
    }

    /**
     * Chequea si un servicio ha sido registrado
     */
    public boolean isRegistered(Class<?> serviceType) {
        return instances.containsKey(serviceType) || singletons.containsKey(serviceType);
    }

    /**
     * Imprime en consola todos los servicios registrados
     */
    public void printRegisteredServices() {
        System.out.println("=== Registered Services ===");
        System.out.println("Instances:");
        instances.keySet().forEach(type -> System.out.println("  " + type.getName()));
        System.out.println("Singletons:");
        singletons.keySet().forEach(type -> System.out.println("  " + type.getName() + " -> " + singletons.get(type).getName()));
    }
}