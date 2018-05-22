package com.dftreactnative;

import android.os.Bundle;

import com.dft.onyxcamera.config.Onyx;
import com.facebook.react.ReactActivity;

/**
 * Created by ericolszewski on 5/22/18.
 * Transmute
 */

public class OnyxActivity extends ReactActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Onyx configuredOnyx = MainApplication.getConfiguredOnyx();
        configuredOnyx.create(this);
        configuredOnyx.capture();
    }
}