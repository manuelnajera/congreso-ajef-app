package com.meb1.manuel.congresoajef;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class lector_qr extends AppCompatActivity {


    private final String LOGTAG = getClass().getSimpleName();
    private CameraSource cameraSource = null;
    private BarcodeDetector barcodeDetector = null;




    public static final int FRONT_CAM = CameraSource.CAMERA_FACING_FRONT;
    public static final int BACK_CAM = CameraSource.CAMERA_FACING_BACK;

    private final int width;
    private final int height;
    private final int facing;
    private final QRDataListener qrDataListener;
    private final Context context;
    private final SurfaceView surfaceView;
    private boolean autoFocusEnabled;

    public FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private boolean cameraRunning = false;

    private boolean surfaceCreated = false;

    public void initAndStart(final SurfaceView surfaceView) {

        surfaceView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        init();
                        start();
                        removeOnGlobalLayoutListener(surfaceView, this);
                    }
                });
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void removeOnGlobalLayoutListener(View v,
                                                     ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }
        else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    private final SurfaceHolder.Callback surfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            //we can start barcode after after creating
            surfaceCreated = true;
            startCameraView(context, cameraSource, surfaceView);
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            surfaceCreated = false;
            stop();
            surfaceHolder.removeCallback(this);
        }
    };

    private lector_qr(final Builder builder) {
        this.autoFocusEnabled = builder.autoFocusEnabled;
        this.width = builder.width;
        this.height = builder.height;
        this.facing = builder.facing;
        this.qrDataListener = builder.qrDataListener;
        this.context = builder.context;
        this.surfaceView = builder.surfaceView;
        //for better performance we should use one detector for all Reader, if builder not specify it
        if (builder.barcodeDetector == null) {
            this.barcodeDetector = BarcodeDetectorHolder.getBarcodeDetector(context);
        }
        else {
            this.barcodeDetector = builder.barcodeDetector;
        }
    }

    public boolean isCameraRunning() {

        return cameraRunning;
    }
    /**
     * Init.
     */
    private void init() {
        if (!hasAutofocus(context)) {
            Log.e(LOGTAG, "Do not have autofocus feature, disabling autofocus feature in the library!");
            autoFocusEnabled = false;
        }

        if (!hasCameraHardware(context)) {
            Log.e(LOGTAG, "Does not have camera hardware!");
            return;
        }
        if (!checkCameraPermission(context)) {
            Log.e(LOGTAG, "Do not have camera permission!");
            return;
        }

        if (barcodeDetector.isOperational()) {
            barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                @Override
                public void release() {
                    // Handled via public method
                }

                @Override
                public void receiveDetections(Detector.Detections<Barcode> detections) {
                    final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                    if (barcodes.size() != 0 && qrDataListener != null) {
                        qrDataListener.onDetected(barcodes.valueAt(0).displayValue);
                    }
                }
            });

            cameraSource =
                    new CameraSource.Builder(context, barcodeDetector).setAutoFocusEnabled(autoFocusEnabled)
                            .setFacing(facing)
                            .setRequestedPreviewSize(width, height)
                            .build();
        }
        else {
            Log.e(LOGTAG, "Barcode recognition libs are not downloaded and are not operational");
        }
    }


    public void start() {
        if (surfaceView != null && surfaceHolderCallback != null) {
            //if surface already created, we can start camera
            if (surfaceCreated) {

                mAuth = FirebaseAuth.getInstance();

                mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(LOGTAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(LOGTAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
                startCameraView(context, cameraSource, surfaceView);
                mAuth.addAuthStateListener(mAuthListener);
            }
            else {
                //startCameraView will be invoke in void surfaceCreated
                surfaceView.getHolder().addCallback(surfaceHolderCallback);
            }
        }
    }

    private void startCameraView(Context context, CameraSource cameraSource,
                                 SurfaceView surfaceView) {
        if (cameraRunning) {
            throw new IllegalStateException("Camera already started!");
        }
        try {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e(LOGTAG, "Permission not granted!");


            }
            else if (!cameraRunning && cameraSource != null && surfaceView != null) {
                cameraSource.start(surfaceView.getHolder());
                cameraRunning = true;
            }
        } catch (IOException ie) {
            Log.e(LOGTAG, ie.getMessage());
            ie.printStackTrace();
        }
    }

    /**
     * Stop camera
     */
    public void stop() {
        try {
            if (cameraRunning && cameraSource != null) {
                cameraSource.stop();
                cameraRunning = false;
            }
        } catch (Exception ie) {
            Log.e(LOGTAG, ie.getMessage());
            ie.printStackTrace();
        }
    }

    /**
     * Release and cleanup QREader.
     */
    public void releaseAndCleanup() {
        stop();
        if (cameraSource != null) {
            //release camera and barcode detector(will invoke inside) resources
            cameraSource.release();
            cameraSource = null;
        }
    }

    private boolean checkCameraPermission(Context context) {
        String permission = android.Manifest.permission.CAMERA;
        int res = context.checkCallingOrSelfPermission(permission);
        return res == PackageManager.PERMISSION_GRANTED;
    }

    private boolean hasCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private boolean hasAutofocus(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS);
    }

    /**
     * The type Builder.
     */
    public static class Builder {
        private boolean autoFocusEnabled;
        private int width;
        private int height;
        private int facing;
        private final QRDataListener qrDataListener;
        private final Context context;
        private final SurfaceView surfaceView;
        private BarcodeDetector barcodeDetector;

        /**
         * Instantiates a new Builder.
         *
         * @param context
         *     the context
         * @param surfaceView
         *     the surface view
         * @param qrDataListener
         *     the qr data listener
         */
        public Builder(Context context, SurfaceView surfaceView, QRDataListener qrDataListener) {
            this.autoFocusEnabled = true;
            this.width = 800;
            this.height = 800;
            this.facing = BACK_CAM;
            this.qrDataListener = qrDataListener;
            this.context = context;
            this.surfaceView = surfaceView;
        }
        /**
         * Enable autofocus builder.
         *
         * @param autofocusEnabled
         *     the autofocus enabled
         * @return the builder
         */
        public Builder enableAutofocus(boolean autofocusEnabled) {
            this.autoFocusEnabled = autofocusEnabled;
            return this;
        }

        /**
         * Width builder.
         *
         * @param width
         *     the width
         * @return the builder
         */
        public Builder width(int width) {
            if (width != 0) {
                this.width = width;
            }
            return this;
        }

        /**
         * Height builder.
         *
         * @param height
         *     the height
         * @return the builder
         */
        public Builder height(int height) {
            if (height != 0) {
                this.height = height;
            }
            return this;
        }

        /**
         * Facing builder.
         *
         * @param facing
         *     the facing
         * @return the builder
         */
        public Builder facing(int facing) {
            this.facing = facing;
            return this;
        }

        /**
         * Build QREader
         *
         * @return the QREader
         */
        public lector_qr build() {
            return new lector_qr(this);
        }

        /**
         * Barcode detector.
         *
         * @param barcodeDetector
         *     the barcode detector
         */
        public void barcodeDetector(BarcodeDetector barcodeDetector) {
            this.barcodeDetector = barcodeDetector;
        }
    }
}