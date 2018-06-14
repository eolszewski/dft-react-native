package com.dftreactnative;

import android.app.Activity;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;

import com.dft.onyx.FingerprintTemplate;
import com.dft.onyx.core;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.opencv.android.OpenCVLoader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by ericolszewski on 5/22/18.
 * Transmute
 */

public class OnyxModule extends ReactContextBaseJavaModule {
    private final static String TAG = OnyxActivity.class.getSimpleName();
    private final static String ENROLL_FILENAME = "enrolled_template.bin";
    private Callback mCallback;

    private static DeviceEventManagerModule.RCTDeviceEventEmitter eventEmitter = null;

    OnyxModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    static {
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Unable to load OpenCV!");
        } else {
            Log.i(TAG, "OpenCV loaded successfully");
            core.initOnyx();
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        eventEmitter = getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
    }

    /**
     * @return the name of this module. This will be the name used to {@code require()} this module
     * from JavaScript.
     */
    @Override
    public String getName() {
        return "Onyx";
    }

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("MyEventName", "MyEventValue");
        constants.put("SaveFingerprint", "SaveFingerprintValue");
        return constants;
    }

    @ReactMethod
    void navigateToOnyx() {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, OnyxActivity.class);
            activity.startActivity(intent);
        }
    }

    @ReactMethod
    void loadEnrolledTemplateIfExists(@Nonnull Promise promise) {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            File enrolledFile = activity.getFileStreamPath(ENROLL_FILENAME);
            if (enrolledFile.exists()) {
                try {
                    FileInputStream enrollStream = activity.openFileInput(ENROLL_FILENAME);
                    ObjectInputStream ois = new ObjectInputStream(enrollStream);
                    FingerprintTemplate mEnrolledTemplate = (FingerprintTemplate) ois.readObject();
                    ois.close();

                    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    final ObjectOutputStream objectOutputStream;
                    try {
                        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                        objectOutputStream.writeObject(mEnrolledTemplate);
                        objectOutputStream.close();
                        String value = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                        promise.resolve(value);
                    } catch (IOException e) {
                        throw new Error(e);
                    }
                } catch (FileNotFoundException e) {
                    promise.reject("FileNotFoundException", e);
                    Log.e(TAG, e.getMessage());
                } catch (StreamCorruptedException e) {
                    promise.reject("StreamCorruptedException", e);
                } catch (IOException e) {
                    promise.reject("IOException", e);
                    Log.e(TAG, e.getMessage());
                } catch (ClassNotFoundException e) {
                    promise.reject("ClassNotFoundException", e);
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    @ReactMethod
    void deleteEnrolledTemplateIfExists() {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            File enrolledFile = activity.getFileStreamPath(ENROLL_FILENAME);
            if (enrolledFile.exists()) {
                enrolledFile.delete();
            }
        }
    }

    @ReactMethod
    void loadEnrolledTemplateFromData(@Nonnull String fingerprintData) {
        Log.i(TAG, "Fingerprint Data: " + fingerprintData);
        Activity activity = getCurrentActivity();
        if (activity != null) {
            try {
                byte[] dataBytes = Base64.decode(fingerprintData, Base64.DEFAULT);
                final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dataBytes);
                final ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream);
                FingerprintTemplate mEnrolledTemplate = (FingerprintTemplate) ois.readObject();
                ois.close();

                FileOutputStream enrollStream = activity.openFileOutput(ENROLL_FILENAME, activity.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(enrollStream);
                oos.writeObject(mEnrolledTemplate);
                oos.close();
            } catch (ClassNotFoundException e) {
                throw new Error(e);
            } catch (FileNotFoundException e) {
                Log.e(TAG, e.getMessage());
            } catch (IOException e) {
                throw new Error(e);
            }
        }
    }

    @ReactMethod
    void loadEnrolledTemplateFileName(@Nonnull Promise promise) {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            File enrolledFile = activity.getFileStreamPath(ENROLL_FILENAME);
            promise.resolve(enrolledFile.getAbsolutePath());
        }
    }

    @ReactMethod
    void enrolledTemplateFileExists(@Nonnull Promise promise) {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            File enrolledFile = activity.getFileStreamPath(ENROLL_FILENAME);
            if (enrolledFile.exists()) {
                promise.resolve(true);
            } else {
                promise.resolve(false);
            }
        }
    }

    @ReactMethod
    void getActivityName(@Nonnull Callback callback) {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            callback.invoke(activity.getClass().getSimpleName());
        }
    }

    @ReactMethod
    void getActivityNameAsPromise(@Nonnull Promise promise) {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            promise.resolve(activity.getClass().getSimpleName());
        }
    }

    /**
     * To pass an object instead of a simple string, create a {@link WritableNativeMap} and populate it.
     */
    static void triggerAlert(boolean message) {
        eventEmitter.emit("MyEventValue", message);
    }
    static void triggerSaveAlert(boolean message) {
        eventEmitter.emit("SaveFingerprintValue", message);
    }

    private void consumeCallback(String msg) {
        if(mCallback!=null) {
            mCallback.invoke(msg);
            mCallback = null;
        }
    }
}