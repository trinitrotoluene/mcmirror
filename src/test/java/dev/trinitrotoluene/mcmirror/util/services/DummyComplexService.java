package dev.trinitrotoluene.mcmirror.util.services;

/** @noinspection unused*/
public final class DummyComplexService {
    private int _flag;

    public DummyComplexService() {
        _flag = 0;
    }

    public DummyComplexService(Integer a, String b) {
        _flag = 1;
    }

    public boolean usedDefaultCtor() {
        return this._flag == 0;
    }

    public boolean usedComplexCtor() {
        return this._flag == 1;
    }
}
