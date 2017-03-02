package com.example.administrator.imageandmaps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.List;


public class Maps_Activity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setTitle("Home");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        StringBuilder userAddress = new StringBuilder();
        LatLng userCurrentLocationCorodinates = new LatLng(MainActivity.userLocation.getLatitude(),MainActivity.userLocation.getLongitude());

        int permissioncheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if(permissioncheck == PackageManager.PERMISSION_GRANTED){
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = managedQuery(MainActivity.ImageUri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath =  cursor.getString(column_index);
            Bitmap bMap = BitmapFactory.decodeFile(filePath);
            Bitmap finalMap = Bitmap.createScaledBitmap(bMap,100,100,false);
            //Setting our image as the marker icon.
            mMap.addMarker(new MarkerOptions().position(userCurrentLocationCorodinates)
                    .title("Your current address.").snippet(userAddress.toString())
                    .icon(BitmapDescriptorFactory.fromBitmap(finalMap)));
            //Setting the zoom level of the map.
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userCurrentLocationCorodinates, 15));
        } else {
            System.out.print("Permission Denied");
        }






    }

}
