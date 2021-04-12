package com.example.mapapplication;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity3 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int RECOGNIZER_RESULT =1;
    SearchView searchView;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps3);
        searchView = findViewById(R.id.sv_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        ImageButton button= findViewById(R.id.voice);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechRecognizer();

            }
        });


        
    }

    // Create an intent that can start the Speech Recognizer activity
    private void speechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);


        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, RECOGNIZER_RESULT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        if (requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK ) {

            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String spokenText = results.get(0);
            searchView.setQuery(spokenText,false);

            String location = searchView.getQuery().toString();
            List<Address> listAddress = null;
            if (location!=null || !location.equals("")){
                Geocoder geocoder= new Geocoder(MapsActivity3.this);
                try{
                    listAddress= geocoder.getFromLocationName(location,1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address= listAddress.get(0);
                LatLng latLng= new LatLng(address.getLatitude(),address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}