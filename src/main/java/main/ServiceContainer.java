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
     * Register an already created instance
     * Similar to C#'s services.AddSingleton(instance)
     */
    public <T> void addInstance(Class<T> serviceType, T instance) {
        instances.put(serviceType, instance);
    }

    /**
     * Register a singleton service (lazy instantiation)
     * Similar to C#'s services.AddSingleton<TInterface, TImplementation>()
     */
    public <TService, TImplementation extends TService> void addSingleton(
            Class<TService> serviceType, 
            Class<TImplementation> implementationType) {
        singletons.put(serviceType, implementationType);
    }

    /**
     * Get a service instance from the container
     * Similar to C#'s serviceProvider.GetService<T>()
     */
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceType) {
        // Check if it's a registered instance
        if (instances.containsKey(serviceType)) {
            return (T) instances.get(serviceType);
        }

        // Check if it's a singleton
        if (singletons.containsKey(serviceType)) {
            return getSingleton(serviceType);
        }

        throw new IllegalArgumentException("Service not registered: " + serviceType.getName());
    }

    @SuppressWarnings("unchecked")
    private <T> T getSingleton(Class<T> serviceType) {
        // Return existing singleton instance if already created
        if (singletonInstances.containsKey(serviceType)) {
            return (T) singletonInstances.get(serviceType);
        }

        // Create new singleton instance with dependency injection
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
                throw new IllegalArgumentException("No public constructors found for: " + type.getName());
            }

            // Use the first constructor (you might want to enhance this logic)
            Constructor<?> constructor = constructors[0];
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];

            // Resolve constructor dependencies recursively
            for (int i = 0; i < parameterTypes.length; i++) {
                parameters[i] = getService(parameterTypes[i]);
            }

            return (T) constructor.newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | 
                 InvocationTargetException e) {
            throw new RuntimeException("Failed to create instance of: " + type.getName(), e);
        }
    }

    /**
     * Check if a service is registered
     */
    public boolean isRegistered(Class<?> serviceType) {
        return instances.containsKey(serviceType) || singletons.containsKey(serviceType);
    }

    /**
     * Get all registered service types (for debugging)
     */
    public void printRegisteredServices() {
        System.out.println("=== Registered Services ===");
        System.out.println("Instances:");
        instances.keySet().forEach(type -> System.out.println("  " + type.getName()));
        System.out.println("Singletons:");
        singletons.keySet().forEach(type -> System.out.println("  " + type.getName() + " -> " + singletons.get(type).getName()));
    }
}