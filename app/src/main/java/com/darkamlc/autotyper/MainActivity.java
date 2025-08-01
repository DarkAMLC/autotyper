package com.darkamlc.autotyper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_TXT_FILE = 1;
    public static Uri selectedFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button selectFileBtn = findViewById(R.id.select_file_btn);
        selectFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTextFile();
            }
        });

        Button startServiceBtn = findViewById(R.id.start_service_btn);
        startServiceBtn.setOnClickListener(v -> {
            if (selectedFileUri != null) {
                Intent serviceIntent = new Intent(this, FloatingButtonService.class);
                serviceIntent.putExtra("fileUri", selectedFileUri.toString());
                startService(serviceIntent);
            } else {
                Toast.makeText(this, "Selecciona un archivo primero.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pickTextFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/plain");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_TXT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_TXT_FILE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                selectedFileUri = data.getData();
                Toast.makeText(this, "Archivo seleccionado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}