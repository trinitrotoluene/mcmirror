package dev.trinitrotoluene.mcmirror.util.services;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class ServiceCollection {
    private final Map<Class, ServiceDescriptor> _services;

    public ServiceCollection() {
        this._services = new HashMap<>();
    }

    public ServiceCollection addSingleton(Class type) {
        var ctor = findBestCtor(type);
        var descriptor = new SingletonServiceDescriptor(ctor);

        this._services.put(type, descriptor);
        return this;
    }

    public <T> ServiceCollection addSingleton(Class type, T instance) {
        var descriptor = new SingletonServiceDescriptor(instance);

        this._services.put(type, descriptor);
        return this;
    }

    public <T> ServiceCollection addSingleton(Class type, Supplier<T> createInstance) {
        var descriptor = new SingletonServiceDescriptor(createInstance);

        this._services.put(type, descriptor);
        return this;
    }

    public ServiceCollection addTransient(Class type) {
        var ctor = findBestCtor(type);
        var descriptor = new TransientServiceDescriptor(ctor);

        this._services.put(type, descriptor);
        return this;
    }

    public <T> ServiceCollection addTransient(Class type, Supplier<T> createInstance) {
        var descriptor = new TransientServiceDescriptor(createInstance);

        this._services.put(type, descriptor);
        return this;
    }

    public IServiceProvider build() throws MissingDependencyException, CircularDependencyException {
        for (var service : this._services.entrySet()) {
            // Check every dependency to see whether it depends on the current service
            for (var dependency : service.getValue().getDependencies()) {
                // If the dependency is not present in the service collection, it is missing
                if (!this._services.containsKey(dependency)) {
                    throw new MissingDependencyException();
                }

                // If the dependency exists **and** its dependencies include the current service, it is circular
                if (Arrays.asList(this._services.get(dependency).getDependencies()).contains(service.getKey())) {
                    throw new CircularDependencyException();
                }
            }
        }

        return new ServiceProvider(this._services);
    }

    private Constructor findBestCtor(Class type) {
        var injectCtor = Arrays.stream(type.getConstructors())
                .filter(c -> c.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());

        if (injectCtor.size() == 1) return injectCtor.get(0);

        return Arrays.stream(type.getConstructors())
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .orElseThrow(() -> new RuntimeException(String.format("Could not resolve a public ctor for service %s",
                        type.getName())));
    }
}
