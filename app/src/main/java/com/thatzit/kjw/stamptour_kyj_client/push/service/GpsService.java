package com.thatzit.kjw.stamptour_kyj_client.push.service;

import android.Manifest;
import android.app.Activity;
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
import com.thatzit.kjw.stamptour_kyj_client.util.MyApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GpsService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "GPS_SERVICE";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final int GPS_PROVIDER_INTERVAL_TIME = 20000;
    private Handler LocationHandler = new Handler();
    private final IBinder mBinder = new MyLocalBinder();
    private boolean m_gpsState = false;
    private Activity myActivity;
    private LocationEventListener myLocationListener;
    private LocationEvent locationEvent;
    private GpsStateEventListener myGpsStateEventListener;
    private GpsStateEvent gpsStateEvent;
    /*
    콘텐츠 다운로드 후 압축해제가 정상적으로 된후에 이벤트 발생시켜주면 리슨하는 리스너와 콘텐츠 다운로드 시작시 발생시킨 이벤트 리슨하는 리스너 필요
    gps값 리슨하는건 상관없지만 리슨한 값과 데이터 비교하여 푸쉬요청보낼 때 필요
    또한 콘텐츠 버전업으로 인해 다시 다운로드 했을 때 알려줘야하기때문에
    현재 메인 액티비티에서 어싱크로 스태틱변수로 데이터 캐쉬하기때문에 캐쉬된 후 이벤트 발생시킴
    처리순서 - 1. 파일 다운시작시 발생한 이벤트 리슨하면 서비스내의 LocatioinChanged리스너에서 어싱크 호출하는 부분 블록(boolean)
             2. 파일 파싱 후 스태틱 어레이에 데이터가 전부 들어가면 정상파싱 이벤트 발생
             3. 2번 이벤트 리슨하면 서비스내의 LocatioinChanged리스너에서 어싱크 호출하는 부분 논블록(boolean)

     */



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
        //mGoogleApiClient.disconnect();
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
                if (myGpsStateEventListener != null)
                    myGpsStateEventListener.OnReceivedStateEvent(gpsStateEvent);
            } else {
                Log.d("GPS-TURNOFF", "off");
                Log.d("mgpsTask-Listen-stop", "off");
                m_gpsState = false;
                mGoogleApiClient.disconnect();
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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("changed Location", location.getLatitude()+"-----"+location.getLongitude());
        locationEvent=new LocationEvent(location);
        if(myLocationListener!=null)myLocationListener.OnReceivedEvent(locationEvent);

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