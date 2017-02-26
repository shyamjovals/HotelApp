package com.example.hhs.hotelapp;

/**
 * Created by hhs on 25/2/17.
 */

import android.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;

import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class Home extends FragmentActivity implements OnMapReadyCallback,SeekBar.OnSeekBarChangeListener {

    SeekBar seekBar1;
    List<ItemObject> allItems;
    private GridLayoutManager lLayout;
    TextView veg,nonveg, range;
    Switch switch1;
    private GoogleMap mMap;
    Marker markerx;
    private final String TAG = getClass().getSimpleName();
    GPSTracker gps;
    RecyclerView rView;
    LatLng mylatlang=new LatLng(0,0);
    public Double curlat, curlong;
    private String[] places;
    private LocationManager locationManager;
    private android.location.Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);



        lLayout = new GridLayoutManager(Home.this, 2);

        rView = (RecyclerView)findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        //getActionBar().setTitle("Vibes");

//        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(Home.this, rowListItem);
//        rView.setAdapter(rcAdapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        seekBar1=(SeekBar)findViewById(R.id.seekBar1);
        seekBar1.setOnSeekBarChangeListener(this);
        seekBar1.setMax(100);
        seekBar1.setProgress(0);

        veg = (TextView) findViewById(R.id.veg);
        nonveg = (TextView) findViewById(R.id.nonveg);
        switch1 = (Switch) findViewById(R.id.switch1);


        veg.setVisibility(View.VISIBLE);
        nonveg.setVisibility(View.INVISIBLE);

        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switch1.isChecked())
                {
                    veg.setVisibility(View.INVISIBLE);
                    nonveg.setVisibility(View.VISIBLE);
                }
                else
                {
                    nonveg.setVisibility(View.INVISIBLE);
                    veg.setVisibility(View.VISIBLE);
                }
            }
        });
        gps=new GPSTracker(Home.this);
        if(gps.canGetLocation()) {

            curlat = gps.getLatitude();
            curlong = gps.getLongitude();
            mylatlang = new LatLng(curlat, curlong);
            currentLocation();
            System.out.println("Your location is " + mylatlang);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        range = (TextView) findViewById(R.id.range);
        //Toast.makeText(getApplicationContext(),"Range : "+progress, Toast.LENGTH_SHORT).show();
        range.setText(String.valueOf(progress));
        int x=progress*1000;
        Dist.radius=String.valueOf(x);
        mMap.clear();
        new GetPlaces(this, "restaurant").execute();


    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        // Add a marker in Sydney and move the camera
        //    LatLng TutorialsPoint = new LatLng(21, 57);
        markerx = mMap.addMarker(new MarkerOptions().position(mylatlang).title("Me").icon(BitmapDescriptorFactory.defaultMarker()));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylatlang));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylatlang, 12.0f));
    }
    private class GetPlaces extends AsyncTask<Void, Void, ArrayList<Place>> {

        private ProgressDialog dialog;
        private Context context;
        private String places;
        BitmapDescriptor desc;

        public GetPlaces(Context context, String places) {
            this.context = context;
            this.places = places;

                    desc= BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);

        }

        @Override
        protected void onPostExecute(ArrayList<Place> result) {
            super.onPostExecute(result);
//            if (dialog.isShowing()) {
//                dialog.dismiss();
//            }
            if(result.size()>0) {
                try {
                    for (int i = 0; i < result.size(); i++) {
                        mMap.addMarker(new MarkerOptions()
                                .title(result.get(i).getName())
                                .position(
                                        new LatLng(result.get(i).getLatitude(), result
                                                .get(i).getLongitude()))
                                .icon(desc)
                                .snippet(result.get(i).getVicinity()));
                    }
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(result.get(0).getLatitude(), result
                                    .get(0).getLongitude())) // Sets the center of the map to
                            // Mountain View
                            .zoom(14) // Sets the zoom
                            .tilt(30) // Sets the tilt of the camera to 30 degrees
                            .build(); // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                    RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(Home.this, allItems);
                    setAdap(rcAdapter);
                } catch (Exception e) {

                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog = new ProgressDialog(context);
//            dialog.setCancelable(false);
//            dialog.setMessage("Loading..");
//            dialog.isIndeterminate();
//            dialog.show();
        }

        @Override
        protected ArrayList<Place> doInBackground(Void... arg0) {
            PlacesService service = new PlacesService(
                    "AIzaSyCVXZqW4O4zDhGOdf36nq23_kzH1LV83ro");
            ArrayList<Place> findPlaces = service.findPlaces(loc.getLatitude(), // 28.632808
                    loc.getLongitude(), places); // 77.218276
            allItems = new ArrayList<ItemObject>();

            for (int i = 0; i < findPlaces.size(); i++) {

                Place placeDetail = findPlaces.get(i);
//set adapter headings here
                Log.e(TAG, "places : " + placeDetail.getName());
                allItems.add(new ItemObject(placeDetail.getName(), R.drawable.rest));

            }

            return findPlaces;
        }

    }


    private void currentLocation() {
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        String provider = locationManager
                .getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Home.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        android.location.Location location = locationManager.getLastKnownLocation(provider);
        System.out.println("Your location iss"+location);
        location=new android.location.Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(curlat);
        location.setLongitude(curlong);


        System.out.println("Your location2 is"+location);
        if (location == null) {
            locationManager.requestLocationUpdates(provider, 0, 0, listener);

        } else {
            loc = location;
            new GetPlaces(this, "restaurant").execute();
//            new GetPlaces(getActivity(), "hospital").execute();
//            new GetPlaces(getActivity(), "doctor").execute();
//            new GetPlaces(getActivity(), "health").execute();
            Log.e(TAG, "location : " + location);
        }

    }

    private LocationListener listener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(android.location.Location location) {
            Log.e(TAG, "location update : " + location);
            loc = location;
            if (ActivityCompat.checkSelfPermission(Home.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Home.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(listener);
        }
    };
    public void setAdap(RecyclerViewAdapter rcAdapter)
    {
        rView.setAdapter(rcAdapter);
    }
}