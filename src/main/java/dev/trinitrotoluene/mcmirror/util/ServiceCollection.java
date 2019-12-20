package dev.trinitrotoluene.mcmirror.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ServiceCollection {
    private final Map<Class, ServiceDescriptor> _services;

    public ServiceCollection() {
        this._services = new HashMap<>();
    }

    public ServiceCollection addSingleton(Class type) {
        // TODO: Check for @Inject annotation when selecting ctor, default to the one with the most parameters.
        var ctor = type.getConstructors()[0];
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
        var ctor = type.getConstructors()[0];
        var descriptor = new TransientServiceDescriptor(ctor);

        this._services.put(type, descriptor);
        return this;
    }

    public <T> ServiceCollection addTransient(Class type, Supplier<T> createInstance) {
        var descriptor = new TransientServiceDescriptor(createInstance);

        this._services.put(type, descriptor);
        return this;
    }

    public IServiceProvider build() throws MissingDependencyException {
        return new ServiceProvider(this._services);
    }
}
