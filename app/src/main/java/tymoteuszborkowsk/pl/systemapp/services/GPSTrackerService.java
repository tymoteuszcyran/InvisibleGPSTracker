package tymoteuszborkowsk.pl.systemapp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GPSTrackerService extends Service implements LocationListener {

    private static String TAG = "INFO";
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES_NETWORK = 10;
    private static final long MIN_TIME_BW_UPDATES_NETWORK = 1000 * 60;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES_GPS = 10;
    private static final long MIN_TIME_BW_UPDATES_GPS = 2000 * 60;

    private String actualCoordinates = "";
    private boolean canGetLocation = false;
    private double latitude;
    private double longitude;

    private Location location;


    protected LocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        final TextFileService textFileService = new TextFileService();
        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (Looper.myLooper() == null)
                    Looper.prepare();

                getLocation();
                if (canGetLocation()) {

                    String coordinates = latitude + ", " + longitude;
                    if (!coordinates.equals(actualCoordinates)) {
                        final NetworkInfo activeNetworks = connectivityManager.getActiveNetworkInfo();
                        actualCoordinates = coordinates;
                        textFileService.createNote(GPSTrackerService.this, coordinates);

                        if (activeNetworks != null) {
                            if ((activeNetworks.getType() == ConnectivityManager.TYPE_WIFI) || (activeNetworks.getType() == ConnectivityManager.TYPE_MOBILE)) {
                                textFileService.uploadNote();
                            }
                        } else {
                            Log.i(TAG, "NETWORK AND WIFI NOT ENABLED");
                        }
                    }
                }
            }
        }, 0, 10, TimeUnit.MINUTES);

    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) getBaseContext().getSystemService(LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.i(TAG, "PROVIDERS ARE DISABLED");
            } else {
                canGetLocation = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES_NETWORK,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES_NETWORK, GPSTrackerService.this);

                    Log.i(TAG, "NETWORK GPS ENABLED");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }

            if (isGPSEnabled) {
                if (location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES_GPS,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES_GPS, GPSTrackerService.this);

                    Log.i(TAG, "HIGH ACCURACY GPS ENABLED");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return location;
    }


    public boolean canGetLocation() {
        return canGetLocation;
    }


    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
