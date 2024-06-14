package com.example.mappointer;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mappointer.models.Point;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

public class MapActivity extends AppCompatActivity {

    private static final String API_KEY_MAP_KIT = "c20a33a5-9458-4106-83a8-54ae976555f2";

    private MapView mapView;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey(API_KEY_MAP_KIT);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBHelper(this);

        mapView = findViewById(R.id.mapview);
        MapKitFactory.initialize(this);

        Intent intent = getIntent();

        Point point = dbHelper.getPointByDescription(intent.getStringExtra("description"));

        mapView.getMap().move(
                new CameraPosition(
                        new com.yandex.mapkit.geometry.Point(
                                Double.parseDouble(point.getLatitude()),
                                Double.parseDouble(point.getLongitude())
                        ),
                        17.5f,
                        0.0f,
                        0.0f));
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
}