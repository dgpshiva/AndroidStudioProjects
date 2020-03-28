package com.example.footsteps;

import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;


public class Tracking extends FragmentActivity implements
        //OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
        private GoogleMap sMap;

        private GoogleApiClient mGoogleApiClient;
        private Location mLastLocation;
        //private MapFragment mapFragment;

        private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
        public static final String TAG = LocationProvider.class.getSimpleName();

        private LocationRequest mLocationRequest;
        private double currentLatitude;
        private double currentLongitude;
        LatLng latLng;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tracking);

            setUpMapIfNeeded();

            //Connecting to Location Services API
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mLastLocation = new Location("");

            // Create the LocationRequest object
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                    .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        }


        private void setUpMapIfNeeded()
        {
            // Do a null check to confirm that we have not already instantiated the map.
            if (sMap == null)
            {
                // Try to obtain the map from the SupportMapFragment.
                sMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                        .getMap();
                // Check if we were successful in obtaining the map.
                if (sMap != null)
                {
                    //setUpMap();
                    sMap.setMyLocationEnabled(true);
                    //Feature to ask user to turn on gps if it is off
                    sMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener()
                    {
                        @Override
                        public boolean onMyLocationButtonClick()
                        {
                            LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            if (!mgr.isProviderEnabled(LocationManager.GPS_PROVIDER))
                            {
                                Toast.makeText(getBaseContext(), "Please turn location on!", Toast.LENGTH_SHORT).show();
                            }
                            return false;
                        }
                    });
                }
            }
        }

        /*private void setUpMap()
        {
            //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
            //Adding button to get My Location
            sMap.setMyLocationEnabled(true);
        }*/

        @Override
        protected void onResume()
        {
            super.onResume();
            setUpMapIfNeeded();
            mGoogleApiClient.connect();
        }

        @Override
        protected void onPause()
        {
            super.onPause();
            if (mGoogleApiClient.isConnected())
            {
                mGoogleApiClient.disconnect();
            }
        }

        @Override
        public void onLocationChanged(Location location)
        {
            currentLatitude = mLastLocation.getLatitude();
            currentLongitude = mLastLocation.getLongitude();
            latLng = new LatLng(currentLatitude, currentLongitude);

            // Instantiates a new Polyline object and traces path of user
            PolylineOptions tracePath = new PolylineOptions()
                    .add(latLng);

            // Instantiates a new Polyline object and adds points to define a rectangle
            /*PolylineOptions rectOptions = new PolylineOptions()
                    .add(new LatLng(37.35, -122.0))
                    .add(new LatLng(37.45, -122.0))  // North of the previous point, but at the same longitude
                    .add(new LatLng(37.45, -122.2))  // Same latitude, and 30km to the west
                    .add(new LatLng(37.35, -122.2))  // Same longitude, and 16km to the south
                    .add(new LatLng(37.35, -122.0)); // Closes the polyline.*/

            // Instantiates a new Polyline object and adds points to define a rectangle
            /*PolylineOptions rectOptions = new PolylineOptions()
                    .add(new LatLng(30.62, -96.34))
                    .add(new LatLng(30.72, -96.34))  // North of the previous point, but at the same longitude
                    .add(new LatLng(30.72, -96.54))  // Same latitude, and 30km to the west
                    .add(new LatLng(30.62, -96.54))  // Same longitude, and 16km to the south
                    .add(new LatLng(30.62, -96.34)); // Closes the polyline.

           sMap.addPolyline(rectOptions);*/
        }


        //Connected to Location Services API
        @Override
        public void onConnected(Bundle connectionHint)
        {
            //Getting current location
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation == null)
            {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
            else
            {
                /*currentLatitude = mLastLocation.getLatitude();
                currentLongitude = mLastLocation.getLongitude();
                latLng = new LatLng(currentLatitude, currentLongitude);

                // Instantiates a new Polyline object and adds points to define a rectangle
                PolylineOptions tracePath = new PolylineOptions()
                        .add(latLng);


                // Get back the mutable Polyline
                //Polyline polyline = sMap.addPolyline(rectOptions);

                sMap.addPolyline(tracePath);*/
            }

            /*if (mLastLocation != null)
            {
                double currentLatitude = mLastLocation.getLatitude();
                double currentLongitude = mLastLocation.getLongitude();
                LatLng latLng = new LatLng(currentLatitude, currentLongitude);

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title("I am here!");
                sMap.addMarker(options);
                sMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }*/
        }

        @Override
        public void onConnectionSuspended(int cause)
        {
            // The connection has been interrupted.
            // Disable any UI components that depend on Google APIs
            // until onConnected() is called.
        }

        @Override
        public void onConnectionFailed(ConnectionResult result)
        {
            // This callback is important for handling errors that
            // may occur while attempting to connect with Google.
            //
            // More about this in the next section.

            if (result.hasResolution())
            {
                try
                {
                    // Start an Activity that tries to resolve the error
                    result.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                }
                catch (IntentSender.SendIntentException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                Log.i(TAG, "Location services connection failed with code " + result.getErrorCode());
            }
        }





        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_tracking, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
}