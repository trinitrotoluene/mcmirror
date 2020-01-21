package dev.trinitrotoluene.mcmirror.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class TickerTests {
    @Test
    void ensureSteppedTicker() {
        var ticker = new SteppedTicker(2, 3);
        assertEquals(0, ticker.getTick());
        ticker.steppedTick();
        ticker.steppedTick();
        assertEquals(0, ticker.getTick());
        ticker.steppedTick();
        assertEquals(1, ticker.getTick());
    }
}
