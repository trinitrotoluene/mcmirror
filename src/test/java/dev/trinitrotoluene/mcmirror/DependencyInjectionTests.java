package dev.trinitrotoluene.mcmirror;

import dev.trinitrotoluene.mcmirror.util.CircularDependencyException;
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
                    .addSingleton(Integer.class, Integer.valueOf(2))
                    .addSingleton(String.class, () -> "Hello, World!")
                    .build();

            assertEquals(Integer.valueOf(2), services.getService(Integer.class));
            assertEquals("Hello, World!", services.getService(String.class));
        }
        catch (MissingDependencyException | CircularDependencyException e) {
            fail();
        }
    }

    @Test
    void ensureResolvesComplexService() {
        try {
            //noinspection UnnecessaryBoxing
            var services = new ServiceCollection()
                    .addSingleton(Integer.class, Integer.valueOf(2))
                    .addSingleton(String.class, "Hello, World!")
                    .addSingleton(DummyComplexService.class)
                    .build();

            DummyComplexService service;
            assertNotNull(service = services.getService(DummyComplexService.class));
            assertTrue(service.usedComplexCtor());
        }
        catch (MissingDependencyException | CircularDependencyException e) {
            fail();
        }
    }

    @Test
    void ensureUsedAnnotatedCtor() {
        try {
            var services = new ServiceCollection()
                    .addSingleton(String.class, "Test")
                    .addSingleton(MarkInjectDummyService.class)
                    .build();

            MarkInjectDummyService service;
            assertNotNull(service = services.getService(MarkInjectDummyService.class));
            assertTrue(service.wasCorrectlyUsed());

        }
        catch (MissingDependencyException | CircularDependencyException e) {
            fail();
        }

    }

    @Test
    void ensureThrowsMissingDependencyInjection() {
        var services = new ServiceCollection()
                .addSingleton(ServiceA.class);

        assertThrows(MissingDependencyException.class, services::build);
    }

    @Test
    void ensureThrowsCircularDependencyInjection() {
        var services = new ServiceCollection()
                .addSingleton(ServiceA.class)
                .addSingleton(ServiceB.class);

        assertThrows(CircularDependencyException.class, services::build);
    }

    @Test
    void ensureBuildsProvider() {
        try {
            var services = new ServiceCollection();

            assertNotNull(services.build());
        }
        catch (MissingDependencyException | CircularDependencyException e) {
            fail();
        }
    }
}
