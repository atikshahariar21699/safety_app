package com.example.robiul.familylocator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Mynavigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback{
    private GoogleMap mMap;
    FirebaseAuth auth;
    GoogleApiClient client;
    LocationRequest request;
    LatLng latLng;
    LocationManager locationManager;
    String name,code,password,email,key;
    HashMap<String, HashMap<String, Double>> locations;
    HashMap<String, HashMap<String, String>> Names;
    String name3="robiul";

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mynavigation);


        Intent myintent=getIntent();
        if(myintent!=null)
        {
            name3=myintent.getStringExtra("Name");
            code=myintent.getStringExtra("Code");
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        auth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }

        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5, 5, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {
                    final   double latitude=location.getLatitude();
                    final    double longitude=location.getLongitude();
                    final    LatLng latLng=new LatLng(latitude, longitude);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Location");
                    myRef.child(code).child("lat").setValue(latitude);
                    myRef.child(code).child("lng").setValue(longitude);
                    myRef.child(code).child("name").setValue(name3);
                    Log.i("hi", "onLocationChanged: "+latitude+longitude);

                    downLocation();

                    Geocoder geocoder=new Geocoder(getApplicationContext());
                    try {
                        String str;
                        List<Address> addressList=geocoder.getFromLocation(latitude,longitude,1);
                       if(addressList!=null) {str=name3+" is in "+addressList.get(0).getLocality()+ " , ";
                        str+=addressList.get(0).getCountryName();}
                        else str = name3;
                        mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,5.2f));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FirebaseDatabase.getInstance().getReference("cycle/"+code).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            HashMap<String,String> myCycle = (HashMap<String, String>) dataSnapshot.getValue();
                            Log.d("TAG", "onDataChange: "+  myCycle);
                            Geocoder geocoder=new Geocoder(getApplicationContext());
                            try {

                                Set<String> keys = myCycle.keySet();
                                Log.d("myTAG", "keys: "+  keys);
                                Log.d("myTAG", "locations: "+ locations);

                                List<String> list = new ArrayList<String>(keys);

                                for(int i=0;i<list.size();i++)
                                {
                                    double latitude = locations.get(list.get(i)).get("lat");
                                    double longitude = locations.get(list.get(i)).get("lng");
                                    String name1=Names.get(list.get(i)).get("name");
                                    Log.d("TAG", "onDataChange:latitude: "+latitude+"  longitude:"+longitude);
                                    String str;
                                    List<Address> addressList=geocoder.getFromLocation(latitude,longitude,1);
                                    if(!addressList.isEmpty()) {str=name1+" is in "+addressList.get(0).getLocality()+ " , ";
                                        str+=addressList.get(0).getCountryName();}
                                    else str = name1;
                                    LatLng latLng=new LatLng(latitude, longitude);
                                    mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                                }
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,5.2f));

                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                     });


                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 05, 5, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {

                    final double latitude=location.getLatitude();
                    final double longitude=location.getLongitude();

                    final LatLng latLng=new LatLng(latitude, longitude);

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Location");
                    //String id=myRef.push().getKey();
                    myRef.child(code).child("lat").setValue(latitude);
                    myRef.child(code).child("lng").setValue(longitude);
                    myRef.child(code).child("name").setValue(name3);
                    Log.i("hi", key+" onLocationChanged2: "+latitude+longitude);
                    downLocation();
                    Geocoder geocoder=new Geocoder(getApplicationContext());
                    try {

                        Toast.makeText(Mynavigation.this, "onDataChange:latitude: "+latitude+"  longitude:"+longitude, Toast.LENGTH_SHORT).show();
                        List<Address> addressList=geocoder.getFromLocation(latitude,longitude,1);

                        String str=name3+" is in "+addressList.get(0).getLocality()+ " , ";
                        str+=addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(str));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,5.2f));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FirebaseDatabase.getInstance().getReference("cycle/"+code).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            HashMap<String,String> myCycle = (HashMap<String, String>) dataSnapshot.getValue();
                            Log.d("TAG", "onDataChange: "+  myCycle);
                            Geocoder geocoder=new Geocoder(getApplicationContext());
                            try {

                                Set<String> keys = myCycle.keySet();
                                Log.d("myTAG", "keys: "+  keys);
                                Log.d("myTAG", "locations: "+ locations);

                                List<String> list = new ArrayList<String>(keys);

                                for(int i=0;i<list.size();i++)
                                {
                                    double latitude = locations.get(list.get(i)).get("lat");
                                    double longitude = locations.get(list.get(i)).get("lng");
                                    String namec = Names.get(list.get(i)).get("name");
                                    Log.d("TAG", "onDataChange:latitude: "+latitude+"  longitude:"+longitude);
                                    List<Address> addressList=geocoder.getFromLocation(latitude,longitude,1);
                                    String str=namec+"  is in "+addressList.get(0).getLocality()+ " , ";
                                    str+=addressList.get(0).getCountryName();
                                    LatLng latLng=new LatLng(latitude, longitude);
                                    mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                                }
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,5.2f));

                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void downLocation() {
        FirebaseDatabase.getInstance().getReference("Location/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                locations = (HashMap<String,HashMap<String, Double> >) dataSnapshot.getValue();
                Names= (HashMap<String,HashMap<String, String> >) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mynavigation, menu);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drive_mode) {

            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.sms_send");
            if (launchIntent != null) {
                startActivity(launchIntent);//null pointer check in case package name was not found
            }
        }
        else if(id==R.id.nav_mycircle)
        {
            Intent myintent = new Intent(this, My_circle.class);
            myintent.putExtra("Code",code);
            myintent.putExtra("Name",name3);
            startActivity(myintent);
            finish();

        }
        else if (id == R.id.devoloper) {
            Intent myintent = new Intent(this, Our_details.class);
            myintent.putExtra("Code",code);
            myintent.putExtra("Name",name3);
            startActivity(myintent);
            finish();


        } else if (id == R.id.show_code) {
            Intent myintent = new Intent(this, Show_id.class);
            myintent.putExtra("Code",code);
            myintent.putExtra("Name",name3);
            startActivity(myintent);
            finish();

        }else if (id == R.id.nav_refresh) {

            Intent myintent = new Intent(this, Mynavigation.class);
            myintent.putExtra("Code",code);
            myintent.putExtra("Name",name3);
            startActivity(myintent);
            finish();

        }
        else if (id == R.id.nav_signout) {

            auth.signOut();
            Intent myintent = new Intent(this, MainActivity.class);
            startActivity(myintent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
