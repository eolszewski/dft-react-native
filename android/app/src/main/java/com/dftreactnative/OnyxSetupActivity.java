package com.dftreactnative;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dft.onyxcamera.config.Onyx;
import com.dft.onyxcamera.config.OnyxConfiguration;
import com.dft.onyxcamera.config.OnyxConfigurationBuilder;
import com.dft.onyxcamera.config.OnyxError;
import com.dft.onyxcamera.config.OnyxResult;

import static com.dftreactnative.ValuesUtil.*;

/**
 * Created by ericolszewski on 5/22/18.
 * Transmute
 */

public class OnyxSetupActivity extends AppCompatActivity {
    private static final int ONYX_REQUEST_CODE = 1337;
    MainApplication application = new MainApplication();
    OnyxConfiguration.SuccessCallback successCallback;
    OnyxConfiguration.ErrorCallback errorCallback;
    OnyxConfiguration.OnyxCallback onyxCallback;
    private Activity activity;
    private Button startOnyxButton;
    private ImageView rawImageView;
    private ImageView processedImageView;
    private ImageView enhancedImageView;
    private TextView livenessResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        setupCallbacks();
    }

    private void setupCallbacks() {
        successCallback = new OnyxConfiguration.SuccessCallback() {
            @Override
            public void onSuccess(OnyxResult onyxResult) {
                application.setOnyxResult(onyxResult);
            }
        };

        errorCallback = new OnyxConfiguration.ErrorCallback() {
            @Override
            public void onError(OnyxError onyxError) {
                Log.e("OnyxError", onyxError.getErrorMessage());
                application.setOnyxError(onyxError);
            }
        };

        onyxCallback = new OnyxConfiguration.OnyxCallback() {
            @Override
            public void onConfigured(Onyx configuredOnyx) {
                application.setConfiguredOnyx(configuredOnyx);
                startOnyxButton.setEnabled(true);
            }
        };
    }

    private void setupOnyx(final Activity activity) {
        // Create an OnyxConfigurationBuilder and configure it with desired options
        OnyxConfigurationBuilder onyxConfigurationBuilder = new OnyxConfigurationBuilder()
                .setActivity(activity)
                .setLicenseKey(getResources().getString(R.string.onyx_license))
                .setReturnRawImage(getReturnRawImage(this))
                .setReturnProcessedImage(getReturnProcessedImage(this))
                .setReturnEnhancedImage(getReturnEnhancedImage(this))
                .setReturnWSQ(getReturnWSQ(this))
                .setReturnFingerprintTemplate(getReturnFingerprintTemplate(this))
                .setShowLoadingSpinner(getShowLoadingSpinner(this))
                .setUseOnyxLive(getUseOnyxLive(this))
                .setUseFlash(getUseFlash(this))
                .setShouldSegment(getShouldSegment(this))
                .setImageRotation(getImageRotation(this))
                .setReticleOrientation(getReticleOrientation(this))
                .setCropSize(getCropSizeWidth(this), getCropSizeHeight(this))
                .setCropFactor(getCropFactor(this))
                .setReticleScale(getReticleScale(this))
                .setLayoutPreference(getLayoutPreference(this))
                .setSuccessCallback(successCallback)
                .setErrorCallback(errorCallback)
                .setOnyxCallback(onyxCallback);
        // Reticle Angle overrides Reticle Orientation so have to set this separately
        if (getReticleAngle(this) != null) {
            onyxConfigurationBuilder.setReticleAngle(getReticleAngle(this));
        }
        // Finally, build the OnyxConfiguration
        onyxConfigurationBuilder.buildOnyxConfiguration();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ONYX_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                displayResults(application.getOnyxResult());
            }
        }
    }

    private void displayResults(OnyxResult onyxResult) {
        rawImageView.setImageDrawable(null);
        processedImageView.setImageDrawable(null);
        enhancedImageView.setImageDrawable(null);
        if (onyxResult.getRawFingerprintBitmap() != null) {
            rawImageView.setImageBitmap(onyxResult.getRawFingerprintBitmap());
        }
        if (onyxResult.getProcessedFingerprintBitmap() != null) {
            processedImageView.setImageBitmap(onyxResult.getProcessedFingerprintBitmap());
        }
        if (onyxResult.getEnhancedFingerprintBitmap() != null) {
            enhancedImageView.setImageBitmap(onyxResult.getEnhancedFingerprintBitmap());
        }
        if (onyxResult.getMetrics() != null) {
            livenessResultTextView.setText(Double.toString(onyxResult.getMetrics().getLivenessConfidence()));
        }
    }

    private void setupUI() {
        activity = this;
        setContentView(R.layout.activity_main);
        rawImageView = (ImageView) findViewById(R.id.rawImageView);
        processedImageView = (ImageView) findViewById(R.id.processedImageView);
        enhancedImageView = (ImageView) findViewById(R.id.enhancedImageView);
        livenessResultTextView = (TextView) findViewById(R.id.livenessResult);
        startOnyxButton = (Button) findViewById(R.id.start_onyx);
        startOnyxButton.setEnabled(false);
        startOnyxButton.bringToFront();
        startOnyxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(activity, OnyxActivity.class), ONYX_REQUEST_CODE);
            }
        });
        Button refreshConfigButton = (Button) findViewById(R.id.refresh_config);
        refreshConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupOnyx(activity);
                startOnyxButton.setEnabled(false);
            }
        });
    }
}