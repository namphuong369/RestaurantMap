package com.nam.restaurantmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView t1, t2;
    LocationDbHelper dbHelper;
    List<Item> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1 = findViewById(R.id.tv1);
        t2 = findViewById(R.id.tv2);
        dbHelper = LocationDbHelper.getInstance(getApplicationContext());
        arrayList = dbHelper.getAllLocation();
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Chưa có địa điểm lưu trữ", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                    intent.putExtra("MAIN", 1);
                    startActivity(intent);
                }
            }
        });
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddActivity.class));
            }
        });
    }
}