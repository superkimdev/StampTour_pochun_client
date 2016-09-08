package com.thatzit.kjw.stamptour_kyj_client.main;

/**
 * Created by kjw on 16. 8. 22..
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.main.fileReader.ReadJson;

public class TabFragment2 extends Fragment implements OnMapReadyCallback{
    MapView mMapView;
    private GoogleMap googleMap;

    private View view;
    private static final LatLng SYDNEY = new LatLng(-33.88,151.21);
    private static final LatLng MOUNTAIN_VIEW = new LatLng(37.4, -122.1);
    private Marker marker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_2, container, false);
        MapSetting(savedInstanceState);
        return view;
    }

    private void MapSetting(Bundle savedInstanceState) {
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        // For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        this.googleMap.setMyLocationEnabled(true);

        // For dropping a marker at a point on the Map
        LatLng kyoungju = new LatLng(35.8533949, 129.1693306);
        Log.e("kyoungju",kyoungju.longitude+":"+kyoungju.latitude);
        for(int i =0 ; i < ReadJson.memCashList.size(); ++i){
            marker= this.googleMap.addMarker(new MarkerOptions().position(new LatLng(35.8533949+i*10, 129.1693306+i*10))
                    .title(ReadJson.memCashList.get(i).getName())
                    .snippet("Marker Description"));
            marker.showInfoWindow();
        }
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kyoungju, 16));

//
//// Zoom in, animating the camera.
//        this.googleMap.animateCamera(CameraUpdateFactory.zoomIn());
//
//// Zoom out to zoom level 10, animating with a duration of 2 seconds.
//        this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
//// Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(MOUNTAIN_VIEW)      // Sets the center of the map to Mountain View
//                .zoom(17)                   // Sets the zoom
//                .bearing(90)                // Sets the orientation of the camera to east
//                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
//                .build();                   // Creates a CameraPosition from the builder
//        this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

}