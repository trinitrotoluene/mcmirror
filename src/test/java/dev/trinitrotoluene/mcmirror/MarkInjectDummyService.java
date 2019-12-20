package dev.trinitrotoluene.mcmirror;

import dev.trinitrotoluene.mcmirror.util.Inject;

/** @noinspection unused*/
public final class MarkInjectDummyService {
    private boolean _used = false;

    public MarkInjectDummyService(Integer i) {
    }

    @Inject
    public MarkInjectDummyService(String s) {
        this._used = true;
    }

    public MarkInjectDummyService(Boolean b) {
    }

    public boolean wasCorrectlyUsed() {
        return this._used;
    }
}
