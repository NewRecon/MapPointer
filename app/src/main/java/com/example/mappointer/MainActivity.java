package com.example.mappointer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mappointer.models.Point;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.mapview.MapView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {

    // geolocation
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private LocationManager locationManager;

    // database
    private DBHelper dbHelper;

    // view
    private Button savePointButton;
    private Button showPointsButton;
    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        savePointButton = findViewById(R.id.savePointButton);

        text = findViewById(R.id.descText);

        dbHelper = new DBHelper(this);

        // GPS
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        savePointButton.setOnClickListener((v) -> {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Point p = new Point(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()), text.getText().toString());
            dbHelper.insertOrUpdatePoints(p);
        });

        showPointsButton = findViewById(R.id.showPointsButton);

        showPointsButton.setOnClickListener(this::startNewActivity);
    }

    public void startNewActivity(View v) {
        Intent intent = new Intent(this, MapListActivity.class);

        startActivity(intent);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Обновление местоположения
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}