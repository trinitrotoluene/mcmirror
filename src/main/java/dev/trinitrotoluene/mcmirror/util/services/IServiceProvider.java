package dev.trinitrotoluene.mcmirror.util.services;

/**
 * Represents a generic IoC container capable of resolving service requests
 */
public interface IServiceProvider {
    /**
     * @param type The type of the service to resolve
     * @param <T> The type of the service to return
     * @return Returns an instance of a service satisfying the provided type constraint
     */
    <T> T getRequiredService(Class type) throws ServiceNotFoundException;

    /**
     * @param type The type of the service to resolve
     * @param <T> The type of the service to return
     * @return Returns an instance of a service satisfying the provided type constraint
     */
    <T> T getService(Class type);
}
