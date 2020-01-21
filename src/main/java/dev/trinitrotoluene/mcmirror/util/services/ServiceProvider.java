package dev.trinitrotoluene.mcmirror.util.services;

import java.util.Map;

/** @noinspection unchecked*/
public final class ServiceProvider implements IServiceProvider {
    private final Map<Class, ServiceDescriptor> _serviceDescriptors;

    ServiceProvider(Map<Class, ServiceDescriptor> serviceDescriptors) {
        this._serviceDescriptors = serviceDescriptors;
    }

    @Override
    public <T> T getRequiredService(Class type) throws ServiceNotFoundException {
        var descriptor = this._serviceDescriptors.entrySet()
                .stream()
                .filter(kvp -> type.isAssignableFrom(kvp.getKey()))
                .findFirst()
                .orElseThrow(() -> new ServiceNotFoundException(String.format("Could not resolve a service matching dependency %s", type.getName())))
                .getValue();

        return (T) descriptor.getInstance(this);
    }

    @Override
    public <T> T getService(Class type) {
        var descriptor = this._serviceDescriptors.entrySet().stream().filter(kvp -> type.isAssignableFrom(kvp.getKey()))
                .findFirst()
                .orElse(null);

        if (descriptor == null) return null;

        return (T) descriptor.getValue().getInstance(this);
    }
}
