package com.nam.restaurantmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.SSLEngineResult;

public class AddActivity extends AppCompatActivity implements LocationListener{
    EditText e1,e2;
    TextView t1,t2,t3;
    LocationDbHelper dbHelper;
 
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        e1=findViewById(R.id.e1);
        e2=findViewById(R.id.e2);
        t1=findViewById(R.id.tv1);
        t2=findViewById(R.id.tv2);
        t3=findViewById(R.id.tv3);
        client = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        Places.initialize(getApplicationContext(),"AIzaSyDhNQLy-ez2DA7t3hNDmrlnQPh1PtuOPTM");
        dbHelper=LocationDbHelper.getInstance(getApplicationContext());
        e2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields= Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
                Intent intent=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fields).build(getApplicationContext());
                startActivityForResult(intent,100);
            }
        });
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurentLocation();
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MapActivity.class);
                String q=e2.getText().toString().trim();
                if(q.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Nh???p thi???u d??? li???u",Toast.LENGTH_SHORT).show();
                }else {
                    if (q.contains(",")) {
                        String[] xs = q.split(",");
                        if(xs.length==2) {
                            try {
                                double x=Double.parseDouble(xs[0]);
                                double y=Double.parseDouble(xs[1]);
                                intent.putExtra("X", xs[0]);
                                intent.putExtra("Y", xs[1]);
                                startActivity(intent);
                            }catch (Exception e)
                            {
                                Toast.makeText(getApplicationContext(),"Sai ki???u d??? li???u",Toast.LENGTH_SHORT).show();

                            }

                        }else{
                            Toast.makeText(getApplicationContext(),"Sai c?? ph??p",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Sai c?? ph??p",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=e1.getText().toString().trim();
                String location=e2.getText().toString().trim();
                if(name.isEmpty()||location.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Nh???p thi???u d??? li???u",Toast.LENGTH_SHORT).show();
                }else{
                    if(location.contains(","))
                    {
                        String[] xs = location.split(",");
                        if(xs.length==2)
                        {
                            try {
                                double x=Double.parseDouble(xs[0]);
                                double y=Double.parseDouble(xs[1]);
                                dbHelper.addLocation(new Item(name, location));
                                Toast.makeText(getApplicationContext(), "Th??m d??? li???u th??nh c??ng", Toast.LENGTH_SHORT).show();
                                finish();
                            }catch (Exception e)
                            {
                                Toast.makeText(getApplicationContext(),"Sai ki???u d??? li???u",Toast.LENGTH_SHORT).show();

                            }



                        }else{
                            Toast.makeText(getApplicationContext(),"Sai c?? ph??p",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Sai c?? ph??p",Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });
    }
    public static boolean isLocationEnabled(Context context)
    {
        //...............
        return true;
    }


    private void getCurentLocation()
    {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
//           try {
//               locationManager=(LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
//               locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5, (LocationListener) AddActivity.this);
//
//           }catch (Exception e)
//           {
//               e.printStackTrace();
//           }


            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        e2.setText(location.getLatitude()+"," +location.getLongitude());
                    }
                }
            });

        }else {
            ActivityCompat.requestPermissions(AddActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 111) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurentLocation();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==RESULT_OK)
        {
            Place place=Autocomplete.getPlaceFromIntent(data);

            e2.setText(""+place.getLatLng().latitude+","+place.getLatLng().longitude);
        }else if(resultCode== AutocompleteActivity.RESULT_ERROR){

            Status status=Autocomplete.getStatusFromIntent(data);
            Log.e("################",status.getStatusMessage());
            Toast.makeText(getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}