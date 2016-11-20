package com.tuliodev.cameratest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private int numCameras;
    private int cameraID;

    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;
    private Button capture;
    private Button swipeCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_preview);

        cameraID = SaveSharePreference.getCameraID(this);
        if(cameraID == -1){
            cameraID = 0;
            SaveSharePreference.setCameraID(this,0);
        }
        numCameras = Camera.getNumberOfCameras();

        preview = (FrameLayout) findViewById(R.id.camera_preview);
        capture = (Button) findViewById(R.id.button_capture);
        swipeCamera = (Button) findViewById(R.id.button_swipeCamera);

        if(numCameras == 1){
            swipeCamera.setVisibility(View.GONE);
        }

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        swipeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newCamera = cameraID + 1;
                preview.removeAllViews();
                if(newCamera < numCameras){
                    cameraID = newCamera;
                    SaveSharePreference.setCameraID(MainActivity.this,newCamera);
                    Camera camera = getCameraInstance(newCamera);
                    releaseCamera();
                    mPreview.swipeCamera(MainActivity.this,camera,newCamera);
                }else{
                    cameraID = 0;
                    SaveSharePreference.setCameraID(MainActivity.this,0);
                    Camera camera = getCameraInstance(0);
                    releaseCamera();
                    mPreview.swipeCamera(MainActivity.this,camera,0);
                }
                preview.addView(mPreview);
            }
        });

        initializeCamera(cameraID);

    }

    void initializeCamera(int cameraID){
        if(checkCameraHardware(this)) {
            // Create an instance of Camera
            mCamera = getCameraInstance(cameraID);

            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mCamera, cameraID);
            preview.addView(mPreview);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SaveSharePreference.getCameraID(this) == -1){
            initializeCamera(cameraID);
        }else{
            initializeCamera(SaveSharePreference.getCameraID(this));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SaveSharePreference.setCameraID(this,0);
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(int cameraID){
        Camera c = null;
        try {
            c = Camera.open(cameraID); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }



    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
}
