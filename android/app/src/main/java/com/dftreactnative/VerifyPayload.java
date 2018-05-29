package com.dftreactnative;

import android.graphics.Bitmap;

import com.dft.onyx.FingerprintTemplate;

/**
 * Created by ericolszewski on 5/29/18.
 * Transmute
 */

public class VerifyPayload {
    private FingerprintTemplate reference;
    private Bitmap probe;

    public VerifyPayload(FingerprintTemplate reference, Bitmap probe) {
        this.setReference(reference);
        this.setProbe(probe);
    }

    public FingerprintTemplate getReference() {
        return reference;
    }

    public void setReference(FingerprintTemplate reference) {
        this.reference = reference;
    }

    public Bitmap getProbe() {
        return probe;
    }

    public void setProbe(Bitmap probe) {
        this.probe = probe;
    }
}
