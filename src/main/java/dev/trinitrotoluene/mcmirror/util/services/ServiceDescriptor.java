package dev.trinitrotoluene.mcmirror.util.services;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class ServiceDescriptor {
    ServiceDescriptor() {
    }

    public abstract Object getInstance(IServiceProvider services);

    public Class[] getDependencies() {
        return new Class[0];
    }

    protected static Object createInstance(Constructor ctor, IServiceProvider services) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        var params = new Object[ctor.getParameterCount()];

        int pos = 0;
        for (var type : ctor.getParameterTypes())
            params[pos++] = services.getService(type);

        return ctor.newInstance(params);
    }
}
