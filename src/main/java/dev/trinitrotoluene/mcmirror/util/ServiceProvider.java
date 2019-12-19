package dev.trinitrotoluene.mcmirror.util;

import java.lang.reflect.Type;

public class ServiceProvider implements IServiceProvider {
    @Override
    public <T> T getRequiredService(Type type) throws ServiceNotFoundException {
        return null;
    }

    @Override
    public <T> T getService(Type type) {
        return null;
    }
}
