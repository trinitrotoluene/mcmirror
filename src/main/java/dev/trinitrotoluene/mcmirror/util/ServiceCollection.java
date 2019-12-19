package dev.trinitrotoluene.mcmirror.util;

import java.util.function.Supplier;

public class ServiceCollection {
    public <T> ServiceCollection addSingleton(T value) {
        return this;
    }

    public <T> ServiceCollection addSingleton(Supplier<T> getInstance) {
        return this;
    }

    public IServiceProvider build() throws MissingDependencyException {
        return new ServiceProvider();
    }
}
