package com.deancampagnolo.csotracker;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapForPast extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng[] mapValues;
    String url = "https://csotracker-c0a65.firebaseio.com/";
    final ArrayList<LatLng> coords = new ArrayList<>();
    private Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_for_past);
        mRef = new Firebase(url);
        LatLng UCSC = new LatLng(36.9916, -122.0583);
        mRef.child("UserInput").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get all of children at the level
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                //Gets every single coordinate
                for(DataSnapshot child : children) {
                    latLng latLneg = child.getValue(latLng.class);
                    LatLng abc = new LatLng(latLneg.getLati(), latLneg.getLongi());
                    coords.add(abc);
                }
            }
            @Override
            public void onCancelled(FirebaseError databaseError) {
            }
        });

        mapValues = new LatLng[1];
        mapValues[0] = UCSC;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    /*
    private void showData(DataSnapshot dataSnapshot){
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            time = dataSnapshot.getValue(String.class);
            latLng LL = new latLng();
            LL.setLati(ds.child(time).getValue(latLng.class).getLati()); //set the name
            LL.setLongi(ds.child(time).getValue(latLng.class).getLongi()); //set the email

           // LatLng abc = new LatLng(LL.getLati(),LL.getLongi());
            coords.add(abc);
        }

    }
     */
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //MapController mapController = mMap.getController;
        // Add a marker in Sydney and move the camera
        //mMap.zoom
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        for(LatLng i : coords){
            MarkerOptions markerOptions = new MarkerOptions().position(i).title("Marker in Sydney");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.jason));
            mMap.addMarker(markerOptions);
        }
        mMap.setMinZoomPreference(15);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mapValues[0]));
        //mMap.moveCamera(CameraUpdateFactory.zoomBy(4));

    }
}
