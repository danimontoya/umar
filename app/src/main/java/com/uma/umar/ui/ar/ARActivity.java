package com.uma.umar.ui.ar;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.uma.umar.R;
import com.uma.umar.model.ARPoint;
import com.uma.umar.ui.BaseActivity;
import com.uma.umar.ui.ar.listener.ArrowsListener;
import com.uma.umar.utils.UMALog;
import com.uma.umar.utils.UmARAnimationUtil;
import com.uma.umar.utils.UmARNetworkUtil;
import com.uma.umar.utils.UmARSharedPreferences;

import java.util.ArrayList;

public class ARActivity extends BaseActivity implements SensorEventListener, LocationListener, ArrowsListener, SeekBar.OnSeekBarChangeListener,
        View.OnClickListener {

    public static final int REQUEST_LOCATION_PERMISSIONS_CODE = 0;
    public static final String AR_POINTS = "arPoints";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ALTITUDE = "altitude";

    private final static String TAG = "ARActivity";
    private final static int REQUEST_CAMERA_PERMISSIONS_CODE = 11;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 60 * 1; // 1 minute
    private static final int DISTANCE_RADIO = 100;
    private static final String FILTER = "FILTER";
    public Location location;
    boolean isGPSEnabled;
    boolean isNetworkEnabled;
    boolean locationServiceAvailable;
    private SurfaceView surfaceView;
    private FrameLayout cameraContainerLayout;
    private AROverlayView arOverlayView;
    private Camera camera;
    private ARCamera arCamera;
    private TextView tvCurrentLocation;
    private SensorManager sensorManager;
    private LocationManager locationManager;

    private ImageView mArrowLeft;
    private ImageView mArrowRight;
    private Animation mShakeAnimationLeft;
    private Animation mShakeAnimationRight;
    private SeekBar mSeekBarDistance;
    private TextView mTextDistanceMeters;
    private ImageView mFilterButton;
    private boolean filterEnabled = true;

    public static void startActivity(Activity activity, ArrayList<ARPoint> arPoints, boolean filterEnabled) {
        if (arPoints == null || !UmARNetworkUtil.isNetworkAvailable()) {
            ((BaseActivity) activity).showDialogNoInternet();
            return;
        }
        Intent intent = new Intent(activity, ARActivity.class);
        intent.putParcelableArrayListExtra(AR_POINTS, arPoints);
        intent.putExtra(FILTER, filterEnabled);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.stay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        // if distance has never been set yet, DISTANCE_RADIO is default.
        if (UmARSharedPreferences.getDistanceRadio() < 0)
            UmARSharedPreferences.setDistanceRadio(DISTANCE_RADIO);

        filterEnabled = getIntent().getExtras().getBoolean(FILTER);

        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        cameraContainerLayout = (FrameLayout) findViewById(R.id.camera_container_layout);
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        tvCurrentLocation = (TextView) findViewById(R.id.tv_current_location);
        mTextDistanceMeters = (TextView) findViewById(R.id.tv_distance_meters);
        mSeekBarDistance = (SeekBar) findViewById(R.id.seekBarDistance);
        mSeekBarDistance.setOnSeekBarChangeListener(this);
        mSeekBarDistance.setProgress((int) UmARSharedPreferences.getDistanceRadio());
        mSeekBarDistance.incrementProgressBy(50);
        mSeekBarDistance.setMax(5000);

        mFilterButton = (ImageView) findViewById(R.id.imageViewFilter);
        mFilterButton.setOnClickListener(this);

        mArrowLeft = (ImageView) findViewById(R.id.arrow_left);
        mArrowRight = (ImageView) findViewById(R.id.arrow_right);

        mShakeAnimationLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        mShakeAnimationRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        mArrowLeft.startAnimation(mShakeAnimationLeft);
        mArrowRight.startAnimation(mShakeAnimationRight);

        ArrayList<ARPoint> arPoints = getIntent().getParcelableArrayListExtra(AR_POINTS);
        arOverlayView = new AROverlayView(getApplicationContext(), arPoints, this);

        initFilters(filterEnabled);
    }

    private void initFilters(boolean filterEnabled) {
        arOverlayView.updateDistanceFilter(filterEnabled);
        mSeekBarDistance.setEnabled(filterEnabled);
        mTextDistanceMeters.setText(getString(R.string.distance_place, filterEnabled ? String.valueOf(UmARSharedPreferences.getDistanceRadio()) + " m" : getString(R.string.filter_disabled)));
        mFilterButton.setImageResource(filterEnabled ? R.mipmap.ic_filter_on : R.mipmap.ic_filter_off);
    }


    @Override
    public void onResume() {
        super.onResume();
        requestLocationPermission();
        requestCameraPermission();
        registerSensors();
        initAROverlayView();
    }

    @Override
    public void onPause() {
        releaseCamera();
        super.onPause();
    }

    public void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSIONS_CODE);
        } else {
            initARCameraView();
        }
    }

    public void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSIONS_CODE);
        } else {
            initLocationService();
        }
    }

    public void initAROverlayView() {
        if (arOverlayView.getParent() != null) {
            ((ViewGroup) arOverlayView.getParent()).removeView(arOverlayView);
        }
        cameraContainerLayout.addView(arOverlayView);
    }

    public void initARCameraView() {
        reloadSurfaceView();

        if (arCamera == null) {
            arCamera = new ARCamera(this, surfaceView);
        }
        if (arCamera.getParent() != null) {
            ((ViewGroup) arCamera.getParent()).removeView(arCamera);
        }
        cameraContainerLayout.addView(arCamera);
        arCamera.setKeepScreenOn(true);
        initCamera();
    }

    private void initCamera() {
        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {
                camera = Camera.open();
                camera.startPreview();
                arCamera.setCamera(camera);
            } catch (RuntimeException ex) {
                Toast.makeText(this, "Camera not found", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void reloadSurfaceView() {
        if (surfaceView.getParent() != null) {
            ((ViewGroup) surfaceView.getParent()).removeView(surfaceView);
        }

        cameraContainerLayout.addView(surfaceView);
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            arCamera.setCamera(null);
            camera.release();
            camera = null;
        }
    }

    private void registerSensors() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float[] rotationMatrixFromVector = new float[16];
            float[] projectionMatrix = new float[16];
            float[] rotatedProjectionMatrix = new float[16];

            SensorManager.getRotationMatrixFromVector(rotationMatrixFromVector, sensorEvent.values);

            if (arCamera != null) {
                projectionMatrix = arCamera.getProjectionMatrix();
            }

            Matrix.multiplyMM(rotatedProjectionMatrix, 0, projectionMatrix, 0, rotationMatrixFromVector, 0);
            this.arOverlayView.updateRotatedProjectionMatrix(rotatedProjectionMatrix);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //do nothing
    }

    private void initLocationService() {

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {
            this.locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

            // Get GPS and network status
            this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isNetworkEnabled && !isGPSEnabled) {
                // cannot get location
                this.locationServiceAvailable = false;
            }

            this.locationServiceAvailable = true;

            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    updateLatestLocation(location);
                }
            }

            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    updateLatestLocation(location);
                }
            }
        } catch (Exception ex) {
            UMALog.e(TAG, ex.getMessage());

        }
    }

    private void updateLatestLocation(Location locationUpdated) {
        if (arOverlayView != null && location != null) {
            arOverlayView.updateCurrentLocation(location);
            tvCurrentLocation.setText(getString(R.string.lat_lon_alt,
                    String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), String.valueOf(location.getAltitude())));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        updateLatestLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void shouldBeVisibleAnyArrow(boolean arrowLeft, boolean arrowRight) {

        UMALog.d(TAG, "arrowLeft=" + arrowLeft + ", " + "arrowRight=" + arrowRight);
        if (!arrowLeft && mShakeAnimationLeft.hasStarted()) {
            mArrowLeft.clearAnimation();
            mArrowLeft.setVisibility(View.GONE);
        }

        if (!arrowRight && mShakeAnimationRight.hasStarted()) {
            mArrowRight.clearAnimation();
            mArrowRight.setVisibility(View.GONE);
        }

        if (arrowLeft && mShakeAnimationLeft.hasEnded()) {
            mArrowLeft.setVisibility(View.VISIBLE);
            mArrowLeft.startAnimation(mShakeAnimationLeft);
        }

        if (arrowLeft && mShakeAnimationRight.hasEnded()) {
            mArrowRight.setVisibility(View.VISIBLE);
            mArrowRight.startAnimation(mShakeAnimationRight);
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        UmARSharedPreferences.setDistanceRadio(progress);
        mTextDistanceMeters.setText(getString(R.string.distance_place, String.valueOf(progress) + " m"));
        if (arOverlayView != null)
            arOverlayView.updateDistance(progress);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mFilterButton.getId()) {
            filterEnabled = !filterEnabled;
            arOverlayView.updateDistanceFilter(filterEnabled);
            mSeekBarDistance.setEnabled(filterEnabled);
            mTextDistanceMeters.setText(getString(R.string.distance_place, filterEnabled ? String.valueOf(UmARSharedPreferences.getDistanceRadio()) + " m" : getString(R.string.filter_disabled)));
            //Animate to cross icon
            UmARAnimationUtil.animateButtonIcon(mFilterButton, filterEnabled ? R.mipmap.ic_filter_on : R.mipmap.ic_filter_off);
        }
    }
}
