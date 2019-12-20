package dev.trinitrotoluene.mcmirror.util;

import java.util.Map;

/** @noinspection unchecked*/
public final class ServiceProvider implements IServiceProvider {
    private final Map<Class, ServiceDescriptor> _serviceDescriptors;

    ServiceProvider(Map<Class, ServiceDescriptor> serviceDescriptors) {
        this._serviceDescriptors = serviceDescriptors;
    }

    @Override
    public <T> T getRequiredService(Class type) throws ServiceNotFoundException {
        if (!this._serviceDescriptors.containsKey(type))
            throw new ServiceNotFoundException();

        return (T) this._serviceDescriptors.get(type).getInstance(this);
    }

    @Override
    public <T> T getService(Class type) {
        if (!this._serviceDescriptors.containsKey(type))
            return null;

        return (T) this._serviceDescriptors.get(type).getInstance(this);
    }
}
