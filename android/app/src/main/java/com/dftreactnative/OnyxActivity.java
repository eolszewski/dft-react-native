package com.dftreactnative;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dft.onyx.FingerprintTemplate;
import com.dft.onyx.core;
import com.dft.onyxcamera.config.Onyx;
import com.dft.onyxcamera.config.OnyxConfiguration;
import com.dft.onyxcamera.config.OnyxConfigurationBuilder;
import com.dft.onyxcamera.config.OnyxError;
import com.dft.onyxcamera.config.OnyxResult;
import com.dft.onyxcamera.ui.reticles.Reticle;
import com.dftreactnative.MainApplication;
import com.dftreactnative.OnyxModule;
import com.dftreactnative.VerifyPayload;
import com.dftreactnative.VerifyTask;
import com.facebook.react.ReactActivity;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

public class OnyxActivity extends ReactActivity {
    private final static String TAG = OnyxActivity.class.getSimpleName();
    private final static String ENROLL_FILENAME = "enrolled_template.bin";

    MainApplication application = new MainApplication();
    OnyxConfiguration.SuccessCallback successCallback;
    OnyxConfiguration.ErrorCallback errorCallback;
    OnyxConfiguration.OnyxCallback onyxCallback;

    private OnyxActivity activity;


    private FingerprintTemplate mCurrentTemplate = null;
    private FingerprintTemplate mEnrolledTemplate = null;

    static {
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Unable to load OpenCV!");
        } else {
            Log.i(TAG, "OpenCV loaded successfully");
            core.initOnyx();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setupCallbacks();
        setupOnyx(activity);
    }

    private void setupCallbacks() {
        successCallback = new OnyxConfiguration.SuccessCallback() {
            @Override
            public void onSuccess(OnyxResult onyxResult) {
                application.setOnyxResult(onyxResult);
                displayResults(onyxResult);
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
                configuredOnyx.create(activity);
                configuredOnyx.capture();
            }
        };
    }

    @Override
    /**
     * This method handles the Android System onResume() callback.
     */
    public void onResume() {
        super.onResume();
        loadEnrolledTemplateIfExists();
    }

    /**
     * This method loads the fingerprint template if it exists.
     */
    private void loadEnrolledTemplateIfExists() {
        File enrolledFile = getFileStreamPath(ENROLL_FILENAME);
        if (enrolledFile.exists()) {
            try {
                FileInputStream enrollStream = openFileInput(ENROLL_FILENAME);
                ObjectInputStream ois = new ObjectInputStream(enrollStream);
                mEnrolledTemplate = (FingerprintTemplate) ois.readObject();
            } catch (FileNotFoundException e) {
                Log.e(TAG, e.getMessage());
            } catch (StreamCorruptedException e) {
                Log.e(TAG, e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (ClassNotFoundException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private void setupOnyx(final Activity activity) {
        // Create an OnyxConfigurationBuilder and configure it with desired options
        OnyxConfigurationBuilder onyxConfigurationBuilder = new OnyxConfigurationBuilder()
                .setActivity(activity)
                .setLicenseKey(getResources().getString(R.string.onyx_license))
                .setReturnRawImage(true)
                .setReturnProcessedImage(true)
                .setReturnEnhancedImage(true)
                .setReturnWSQ(false)
                .setReturnFingerprintTemplate(true)
                .setShowLoadingSpinner(true)
                .setUseOnyxLive(false)
                .setUseFlash(true)
                .setShouldSegment(false)
                .setImageRotation(0)
                .setReticleOrientation(Reticle.Orientation.LEFT)
                .setCropSize(512.0, 300.0)
                .setCropFactor(1.0f)
                .setReticleScale(1.0f)
                .setLayoutPreference(OnyxConfiguration.LayoutPreference.FULL)
                .setSuccessCallback(successCallback)
                .setErrorCallback(errorCallback)
                .setOnyxCallback(onyxCallback);


        // Finally, build the OnyxConfiguration
        onyxConfigurationBuilder.buildOnyxConfiguration();
    }

    private void displayResults(OnyxResult onyxResult) {
        Log.i(TAG, "Quality: " + onyxResult.getMetrics().getFocusQuality());
        if (mEnrolledTemplate != null) {
            VerifyTask verifyTask = new VerifyTask(getApplicationContext());
            verifyTask.execute(new VerifyPayload(mEnrolledTemplate, onyxResult.getProcessedFingerprintImage()));
            finish();
        } else if (onyxResult.getMetrics().getFocusQuality() > 50) {
            Toast.makeText(getApplicationContext(), "Saving Fingerprint Template", Toast.LENGTH_SHORT).show();
            mCurrentTemplate = onyxResult.getFingerprintTemplate();
            enrollCurrentTemplate();
            OnyxModule.triggerSaveAlert(true);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_SHORT).show();
            Onyx configuredOnyx = MainApplication.getConfiguredOnyx();
            configuredOnyx.create(activity);
            OnyxModule.triggerAlert(false);
            configuredOnyx.capture();
        }
    }

    private void enrollCurrentTemplate() {
        mEnrolledTemplate = mCurrentTemplate;
        deleteEnrolledTemplateIfExists();

        try {
            FileOutputStream enrollStream = this.openFileOutput(ENROLL_FILENAME, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(enrollStream);
            oos.writeObject(mEnrolledTemplate);
            oos.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void deleteEnrolledTemplateIfExists() {
        File enrolledFile = getFileStreamPath(ENROLL_FILENAME);
        if (enrolledFile.exists()) {
            enrolledFile.delete();
        }
    }
}