package dev.trinitrotoluene.mcmirror;

import dev.trinitrotoluene.mcmirror.util.MissingDependencyException;
import dev.trinitrotoluene.mcmirror.util.ServiceCollection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class DependencyInjectionTests {
    @Test
    void ensureResolvesBasicService() {
        try {
            //noinspection UnnecessaryBoxing
            var services = new ServiceCollection()
                    .addSingleton(Integer.valueOf(2))
                    .addSingleton(() -> "Hello, World!")
                    .build();

            assertEquals(Integer.valueOf(2), services.getService(Integer.class));
            assertEquals("Hello, World!", services.getService(String.class));
        }
        catch (MissingDependencyException e) {
            fail();
        }
    }

    @Test
    void ensureResolvesComplexService() {
        try {
            //noinspection UnnecessaryBoxing
            var services = new ServiceCollection()
                    .addSingleton(Integer.valueOf(2))
                    .addSingleton("Hello, World!")
                    .addSingleton(DummyComplexService.class)
                    .build();

            assertNotNull(services.getService(DummyComplexService.class));
        }
        catch (MissingDependencyException ex) {
            fail();
        }
    }

    @Test
    void ensureThrowsWhenDependenciesMissing() {
        var services = new ServiceCollection().addSingleton(DummyComplexService.class);

        assertThrows(MissingDependencyException.class, services::build);
    }

    @Test
    void ensureBuildsProvider() {
        try {
            var services = new ServiceCollection();

            assertNotNull(services.build());
        }
        catch (MissingDependencyException e) {
            fail();
        }
    }
}
