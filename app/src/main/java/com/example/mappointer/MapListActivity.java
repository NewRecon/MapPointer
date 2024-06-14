package com.example.mappointer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mappointer.models.Point;

import java.util.List;

public class MapListActivity extends AppCompatActivity {

    private Spinner mapList;
    private DBHelper dbHelper;

    private String text;

    private Button btn;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mapList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mapList = findViewById(R.id.list_item);

        dbHelper = new DBHelper(this);

        List<Point> list = dbHelper.getAllPoints();

        ArrayAdapter<Point> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mapList.setAdapter(adapter);

        btn = findViewById(R.id.btn);

        btn.setOnClickListener(v -> {
            String source = mapList.getSelectedItem().toString();
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("description", source);
            startActivity(intent);
        });
    }

    public void startNewActivity(View v) {
        TextView tv = (TextView) v;
        text = tv.getText().toString();

        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("description", text);
        startActivity(intent);
    }
}