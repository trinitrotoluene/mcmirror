package dev.trinitrotoluene.mcmirror.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public final class SingletonServiceDescriptor extends ServiceDescriptor {
    private Supplier _createInstance;
    private Constructor _ctor;
    private Object _instance;

    SingletonServiceDescriptor(Constructor ctor) {
        this._ctor = ctor;
    }

    SingletonServiceDescriptor(Supplier createInstance) {
        this._createInstance = createInstance;
    }

    SingletonServiceDescriptor(Object instance) {
        this._instance = instance;
    }

    @Override
    public synchronized Object getInstance(IServiceProvider services) {
        if (this._instance != null)
            return this._instance;

        if (this._createInstance != null) {
            this._instance = this._createInstance.get();
            return this._instance;
        }

        try {
            this._instance = ServiceDescriptor.createInstance(this._ctor, services);
            return this._instance;
        }
        catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
