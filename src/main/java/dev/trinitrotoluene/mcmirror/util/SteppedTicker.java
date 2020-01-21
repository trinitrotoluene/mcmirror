package dev.trinitrotoluene.mcmirror.util;

public final class SteppedTicker extends Ticker {
    private final Ticker _ticker;

    public SteppedTicker(int maxVal, int step) {
        super(maxVal);
        this._ticker = new Ticker(step);
    }

    public synchronized int steppedTick() {
        if (this._ticker.tickNext() == 0) {
            this.tickNext();
        }
        return this._count;
    }
}
