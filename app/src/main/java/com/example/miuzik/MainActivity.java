package com.example.miuzik;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.Manifest;
import android.provider.MediaStore;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FilesAdapter adapter;
    private List<String> data;

    private static final int REQUEST_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        data = new ArrayList<>();
        adapter = new FilesAdapter(data);
        recyclerView.setAdapter(adapter);

        // Sprawdź i poproś o uprawnienia
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_AUDIO}, REQUEST_PERMISSION);
            } else {
                loadAudioFiles();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            } else {
                loadAudioFiles();
            }
        }
    }

    // Obsługa odpowiedzi na żądanie uprawnień
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadAudioFiles();
        }
    }

    private void loadAudioFiles() {
        List<String> audioFiles = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA
        };

        Cursor cursor = getContentResolver().query(uri, projection,
                MediaStore.Audio.Media.IS_MUSIC + "!= 0",
                null, null);

        if (cursor != null) {
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            while (cursor.moveToNext()) {
                String title = cursor.getString(titleColumn);
                audioFiles.add(title);
            }
            cursor.close();
        }

        adapter.setItems(audioFiles);
    }
}
