package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.DataOutputStream;

public class MainActivity extends AppCompatActivity {
    private Button allowPermission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allowPermission=findViewById(R.id.allowPermission);
        allowPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
//                Settings.Secure.putString(getContentResolver(),
//                        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, "com.example.myapplication/MyAccessibilityService");
//                Settings.Secure.putString(getContentResolver(),
//                        Settings.Secure.ACCESSIBILITY_ENABLED, "1");
            }
        });
    }

}