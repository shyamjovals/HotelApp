package com.example.hhs.hotelapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import static android.content.ContentValues.TAG;

/**
 * Created by hhs on 25/2/17.
 */

public class Location extends Fragment implements OnMapReadyCallback{
GPSTracker gps;
    private GoogleMap mMap;
    Marker markerx;
    LatLng mylatlang=new LatLng(0,0);
    public Double curlat, curlong;
    private LocationManager locationManager;
    private android.location.Location loc;
    public Location() {
        // Required empty public constructor
    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.location, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        gps=new GPSTracker(getActivity());
        if(gps.canGetLocation()) {

            curlat = gps.getLatitude();
            curlong = gps.getLongitude();
            mylatlang = new LatLng(curlat, curlong);
            currentLocation();
            System.out.println("Your location is " + mylatlang);
        }
        return parentView;

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
                        if(result.get(i).getName().equals(Hotel_indi.hotel_name))
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
      

            for (int i = 0; i < findPlaces.size(); i++) {

                Place placeDetail = findPlaces.get(i);
//set adapter headings here
                Log.e(TAG, "places : " + placeDetail.getName());

            }

            return findPlaces;
        }

    }


    private void currentLocation() {
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        String provider = locationManager
                .getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            new GetPlaces(getActivity(), "restaurant").execute();
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
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
}
