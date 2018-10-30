package com.example.anderson.subcsparql.Application;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.anderson.subcsparql.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class LSDiSP extends FragmentActivity implements OnMapReadyCallback,
        LocationListener, GoogleMap.OnMarkerClickListener {


    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    Marker mCurrentLocation;
    private static final String EXTRA_ORIG = "orig";
    private static final String EXTRA_DEST = "dest";
    private static final String EXTA_ROTA = "rota";
    LoaderManager loaderManager;

    LatLng mOrigem;
    LatLng mDestino;
    ArrayList<LatLng> mRota;

    private static final String TAG = "LSDiSP";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private FusedLocationProviderClient mFusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsdi_sp);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getLocationPermission();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Habilitar click  no mapa
        mMap.getUiSettings().setMapToolbarEnabled(true);
        //Habilitar zoom
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng v1 = new LatLng(-2.557785, -44.308088);
        LatLng v2 = new LatLng(-2.557785, -44.30807);
        LatLng v3 = new LatLng(-2.557785, -44.308052);
        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(v1).title("Marker in Space 1").icon(bitmapDescriptorFromVector(this, R.drawable.ic_busy)));
        mMap.addMarker(new MarkerOptions().position(v2).title("Marker in Space 2").icon(bitmapDescriptorFromVector(this, R.drawable.ic_free)));
        mMap.addMarker(new MarkerOptions().position(v3).title("Marker in Space 3").icon(bitmapDescriptorFromVector(this, R.drawable.ic_free)));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(v1, 20));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);
        //getDeviceLocation();
        //rota();
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private LatLng getDeviceLocation() {
        final LatLng[] device = new LatLng[1];

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionsGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            device[0] = new LatLng(currentLocation.getLatitude(),
                                    currentLocation.getLongitude());


                            // latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

                            mCurrentLocation = mMap.addMarker(new MarkerOptions().position(device[0]).title("Device"));
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(LSDiSP.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }

        return device[0];
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    private void rota(LatLng origem, LatLng destino) {
        GoogleDirection.withServerKey(getString(R.string.google_direction))
                .from(origem)
                .to(destino)
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            int legCount = route.getLegList().size();
                            for (int index = 0; index < legCount; index++) {
                                Leg leg = route.getLegList().get(index);
                                List<Step> stepList = leg.getStepList();
                                ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.
                                        createTransitPolyline(LSDiSP.this,
                                                stepList, 5, Color.BLUE, 3, Color.BLUE);
                                for (PolylineOptions polylineOption : polylineOptionList) {
                                    mMap.addPolyline(polylineOption);
                                }
                            }
                            setCameraWithCoordinationBounds(route);

                        } else {
                            Toast.makeText(LSDiSP.this, "Erro na conexão", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        t.printStackTrace();
                    }
                });
    }


    public void setMarker(LatLng latLng, String title) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.mipmap.marker);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(bitmapDescriptor)
                .title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        LatLng latLng = getDeviceLocation();

        if (latLng != null) {
            rota(latLng, new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));

        }
        //Toast.makeText(this, "Clico no " + marker.getTitle(), Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        Toast.makeText(this, "mudou ", Toast.LENGTH_SHORT).show();

    }
}
