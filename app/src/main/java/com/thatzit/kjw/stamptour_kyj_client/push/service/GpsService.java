package com.thatzit.kjw.stamptour_kyj_client.push.service;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.push.service.event.GpsStateEvent;
import com.thatzit.kjw.stamptour_kyj_client.push.service.event.LocationEvent;
import com.thatzit.kjw.stamptour_kyj_client.push.service.fileReader.InServiceLoadAsyncTask;
import com.thatzit.kjw.stamptour_kyj_client.push.service.msgListener.GpsStateEventListener;
import com.thatzit.kjw.stamptour_kyj_client.push.service.msgListener.LocationEventListener;
import com.thatzit.kjw.stamptour_kyj_client.util.MyApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GpsService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final String TAG = "GPS_SERVICE";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final int GPS_PROVIDER_INTERVAL_TIME = 10000;
    private Handler LocationHandler = new Handler();
    private final IBinder mBinder = new MyLocalBinder();
    private boolean m_gpsState = false;
    private Activity myActivity;
    private LocationEventListener myLocationListener;
    private LocationEvent locationEvent;
    private GpsStateEventListener myGpsStateEventListener;
    private GpsStateEvent gpsStateEvent;
    private NotificationManager mNotificationManager;
    private PreferenceManager preferenceManager;
    private boolean downFlag=true;


    public class MyLocalBinder extends Binder {
        public GpsService getService(){
            return GpsService.this;
        }
    }
    public Activity getMyActivity() {
        return myActivity;
    }

    public void setMyActivity(Activity myActivity) {
        this.myActivity = myActivity;
        mGoogleApiClient = new GoogleApiClient.Builder(myActivity).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                build();
        m_gpsState = true;
        mGoogleApiClient.connect();
        Log.d(TAG, "setMyActivity");
    }

    public void afterBulid() {
        if(mGoogleApiClient!=null){
            if(mGoogleApiClient.isConnected()) mGoogleApiClient.disconnect();
        }
        mGoogleApiClient = new GoogleApiClient.Builder(MyApplication.getContext()).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                build();
        m_gpsState = true;
        mGoogleApiClient.connect();
        Log.d(TAG, "afterBulid");
    }

    public void gpsCheckStart() {
        Log.d(TAG, "gpsCheckStart");
        LocationHandler.postDelayed(mgpsTask, 2000);
    }

    private Runnable mgpsTask = new Runnable() {
        public void run() {
            boolean isGPSTurnOn = CheckGPSIsRunningUtil.onCheckGPSIsRunning();
            if (isGPSTurnOn) {
                Log.d("GPS-TURNON", "on");
                if (m_gpsState == false) {
                    Log.d("mgpsTask-Listen-start", "on");
                    afterBulid();
                }
                gpsStateEvent = new GpsStateEvent(isGPSTurnOn);
                if (myGpsStateEventListener != null) myGpsStateEventListener.OnReceivedStateEvent(gpsStateEvent);
                else Log.d(TAG,"myGpsStateEventListener is null");
            } else {
                Log.d("GPS-TURNOFF", "off");
                Log.d("mgpsTask-Listen-stop", "off");
                m_gpsState = false;
                if(mGoogleApiClient!=null){
                    if(mGoogleApiClient.isConnected()) mGoogleApiClient.disconnect();
                }
                gpsStateEvent = new GpsStateEvent(isGPSTurnOn);
                if (myGpsStateEventListener != null)
                    myGpsStateEventListener.OnReceivedStateEvent(gpsStateEvent);
            }
            LocationHandler.postDelayed(this, 2000);
        }
    };

    public void gpsCheckStop() {
        Log.d(TAG, "gpsCheckStop");
        ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();
        Future<?> longRunningTaskFuture = threadPoolExecutor.submit(mgpsTask);
        // At some point in the future, if you want to kill the task:
        longRunningTaskFuture.cancel(true);
        LocationHandler.removeCallbacks(mgpsTask);
        mGoogleApiClient.disconnect();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        gpsCheckStart();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        gpsCheckStop();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(GPS_PROVIDER_INTERVAL_TIME);
        mLocationRequest.setFastestInterval(GPS_PROVIDER_INTERVAL_TIME);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        Log.d(TAG, "Request Location");
        preferenceManager = new PreferenceManager(MyApplication.getContext());
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("changed Location", location.getLatitude()+":"+location.getLongitude());
        locationEvent=new LocationEvent(location);
        if(myLocationListener!=null)myLocationListener.OnReceivedEvent(locationEvent);
        downFlag=preferenceManager.getDownFlag();
        if(downFlag == false) {
            //Load contents, download finished
            Log.e(TAG,"DownLoading contents finished");
            new InServiceLoadAsyncTask(location, MyApplication.getContext()).execute();
        }else{
            //current contents downloading...
            Log.e(TAG,"DownLoading contents...");
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    /**
     * LocationEventListener Set Method
     * @param listener
     */
    public void setOnLocationEventListener(LocationEventListener listener) {
        this.myLocationListener = listener;
    }
    /**
     * GpsStateEventListener Set Method
     * @param listener
     */
    public void setOnGpsStateEventListener(GpsStateEventListener listener) {
        this.myGpsStateEventListener = listener;
    }
}