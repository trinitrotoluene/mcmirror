package dev.trinitrotoluene.mcmirror.util.services;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class TransientServiceDescriptor extends ServiceDescriptor {
    private Constructor _ctor;
    private Supplier _createInstance;

    public TransientServiceDescriptor(Constructor ctor) {
        this._ctor = ctor;
    }

    public TransientServiceDescriptor(Supplier createInstance) {
        this._createInstance = createInstance;
    }

    @Override
    public Class[] getDependencies() {
        if (this._ctor != null)
            return this._ctor.getParameterTypes();

        return super.getDependencies();
    }

    @Override
    public synchronized Object getInstance(IServiceProvider services) {
        if (this._createInstance != null)
            return this._createInstance.get();

        try {
            return ServiceDescriptor.createInstance(this._ctor, services);
        }
        catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
