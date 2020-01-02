package dev.trinitrotoluene.mcmirror.util.commands;

import dev.trinitrotoluene.mcmirror.util.services.CircularDependencyException;
import dev.trinitrotoluene.mcmirror.util.services.MissingDependencyException;
import dev.trinitrotoluene.mcmirror.util.services.ServiceCollection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class CommandServiceTests {
    @Test
    void ensureBuilds() throws CircularDependencyException, MissingDependencyException {
        var services = new ServiceCollection()
                .build();
        var commands = new CommandServiceBuilder()
                .build(services);

        assertNotNull(commands);
    }

    @Test
    void ensureExecutesTopLevel() throws Throwable {
        var services = new ServiceCollection()
                .addSingleton(TestModuleA.class)
                .build();

        var commands = new CommandServiceBuilder()
                .registerCommands(TestModuleA.class)
                .build(services);

        var result = commands.execute("foo", new CommandContext());

        assertTrue(result);
    }

    @Test
    void ensureSetsContext() throws Throwable {
        var services = new ServiceCollection()
                .addSingleton(TestModuleA.class)
                .build();

        var commands = new CommandServiceBuilder()
                .registerCommands(TestModuleA.class)
                .build(services);

        assertTrue(commands.execute("contextisnull", null));
        assertFalse(commands.execute("contextisnull", new CommandContext()));
    }

    @Test
    void ensureExecutesSubcommands() throws Throwable {
        var services = new ServiceCollection()
                .addSingleton(TestModuleB.class)
                .addSingleton(TestModuleB.TestModuleC.class)
                .build();

        var commands = new CommandServiceBuilder()
                .registerCommands(TestModuleB.class)
                .build(services);

        assertTrue(commands.execute("foo bar", new CommandContext()));
        assertTrue(commands.execute("foo baz bar", new CommandContext()));
    }
}
