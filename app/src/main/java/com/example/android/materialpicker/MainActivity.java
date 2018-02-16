package com.example.android.materialpicker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.util.jar.Manifest;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    Button button;
    ImageButton ib;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }

        button= (Button) findViewById(R.id.button);
        textView= (TextView)findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialFilePicker()
                        .withActivity(MainActivity.this)
                        .withRequestCode(1000)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ib= (ImageButton)findViewById(R.id.imageButton);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
             String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            // Do anything with file
            textView.setText(filePath);
            final String fp= textView.toString();

            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, Groupify.class);
                    i.putExtra("path", fp);
                    startActivity(i);
                    finish();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int [] grantResults) {
        switch(requestCode){
            case 1001: {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }


}
