package dev.trinitrotoluene.mcmirror.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class ServiceDescriptor {
    ServiceDescriptor() {
    }

    public abstract Object getInstance(IServiceProvider services);

    protected static Object createInstance(Constructor ctor, IServiceProvider services) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        var params = new Object[ctor.getParameterCount()];

        int pos = 0;
        for (var type : ctor.getParameterTypes())
            params[pos++] = services.getService(type);

        return ctor.newInstance(params);
    }
}
