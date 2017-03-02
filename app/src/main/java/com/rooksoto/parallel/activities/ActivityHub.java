package com.rooksoto.parallel.activities;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.rooksoto.parallel.R;
import com.rooksoto.parallel.geolocation.ParallelLocation;
import com.rooksoto.parallel.utility.CustomAlertDialog;
import com.rooksoto.parallel.utility.CustomSoundEffects;

public class ActivityHub extends AppCompatActivity {
    private CustomSoundEffects mCustomSoundEffects;
    private CustomAlertDialog mCustomAlertDialog = new CustomAlertDialog();

    private static final String TAG = "ActivityHub";
    ParallelLocation locationService = null;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkForGoogleApiAvail();
        if(Build.VERSION.SDK_INT >= 23) {
            getLocationPermissions();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationServices();
    }

    private void checkForGoogleApiAvail() {
        int hasGpsInstalled = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (hasGpsInstalled != ConnectionResult.SUCCESS) {
            Log.d(TAG, "Google API Not Available on This Device");
            GoogleApiAvailability.getInstance().getErrorDialog(this, hasGpsInstalled, 1).show();
        } else {
            Log.d(TAG, "Google API is Available");
        }
    }

    private void startLocationServices() {
        locationService = ParallelLocation.getInstance();
        locationService.connect();
        locationService.startGeofenceMonitoring();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocationPermissions() {
        requestPermissions(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION},
        9999);

    }

    private void initialize () {
        mCustomSoundEffects = new CustomSoundEffects(getWindow().getDecorView().getRootView());
    }

    @Override
    public void onBackPressed () {
        mCustomAlertDialog.exit(this);
        //super.onBackPressed();
    }
}