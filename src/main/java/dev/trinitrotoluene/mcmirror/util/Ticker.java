package dev.trinitrotoluene.mcmirror.util;

public class Ticker {
    protected final int _maxVal;
    protected int _count = 0;

    public Ticker (int maxVal) {
        this._maxVal = maxVal;
    }

    public synchronized int getTick() {
        return this._count;
    }

    protected synchronized int tickNext() {
        this._count = (this._count + 1) % this._maxVal;
        return this._count;
    }
}
