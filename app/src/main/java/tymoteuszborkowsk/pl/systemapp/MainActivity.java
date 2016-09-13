package tymoteuszborkowsk.pl.systemapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tymoteuszborkowsk.pl.systemapp.services.GPSTrackerService;


public class MainActivity extends AppCompatActivity {


    private static final int PERMMISION_REQUEST_CODE = 200;

    @Override
    @TargetApi(23)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (shouldAskPermission()) {
            String perms[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                              Manifest.permission.ACCESS_FINE_LOCATION,
                              Manifest.permission.ACCESS_COARSE_LOCATION,
                              Manifest.permission.READ_EXTERNAL_STORAGE,
                              Manifest.permission.ACCESS_NETWORK_STATE,
                              Manifest.permission.ACCESS_WIFI_STATE};

            if (!hasPermission(perms))
                requestPermissions( perms, PERMMISION_REQUEST_CODE);

        }else
            startService(new Intent(this, GPSTrackerService.class));


    }



    private boolean shouldAskPermission() {

        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 200:
                startService(new Intent(this, GPSTrackerService.class));
                break;

        }
    }

    @SuppressLint("NewApi")
    private boolean hasPermission(String[] permissions) {
        boolean[] areGranted = new boolean[permissions.length];
        boolean output = false;

        for (int i = 0; i < permissions.length; i++) {
            boolean canMakeSmores = Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
            areGranted[i] = !canMakeSmores || (checkSelfPermission(permissions[i]) == PackageManager.PERMISSION_GRANTED);
        }

        for (boolean isGranted : areGranted) {
            if (!isGranted) {
                break;
            } else
                output = true;
        }

        return output;
    }
}