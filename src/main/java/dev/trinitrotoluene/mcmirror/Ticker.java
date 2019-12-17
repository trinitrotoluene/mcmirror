package dev.trinitrotoluene.mcmirror;

final class Ticker {
    private final int _maxVal;
    private int _count = 0;

    public Ticker (int maxVal) {
        this._maxVal = maxVal;
    }

    public synchronized int getTick() {
        return this._count;
    }

    public synchronized int tickNext() {
        this._count = (this._count + 1) % this._maxVal;
        return this._count;
    }
}
