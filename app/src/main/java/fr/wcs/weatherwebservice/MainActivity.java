package fr.wcs.weatherwebservice;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.GsonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static final int MY_REQUEST_FOR_LOCATION = 1;
    private double latitude;
    private double longitude;
    private TextView textViewCurrent;
    private TextView textViewForecast;

    private String apiKey;

    private LocationManager mLocationManager = null;
    private LocationListener mLocationListener;

    protected SpiceManager spiceManager = new SpiceManager(GsonGoogleHttpClientSpiceService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewCurrent = (TextView) findViewById(R.id.textViewCurrent);

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        apiKey = "34ee32ab54364b6d1e459101c08ca3c2";
        mLocationListener = new LocationListener() {


            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Toast.makeText(MainActivity.this, location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();
                performRequest();
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
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_REQUEST_FOR_LOCATION);
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);

        spiceManager.start(this);

    }

    @Override
    protected void onStop() {
        super.onStop();

        mLocationManager.removeUpdates(mLocationListener);
        spiceManager.shouldStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 20, mLocationListener);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 20, mLocationListener);

    }

    private void performRequest() {

        MainActivity.this.setProgressBarIndeterminateVisibility(true);

        ForecastWeatherRequest requestCurrent = new ForecastWeatherRequest(latitude, longitude, apiKey);
        spiceManager.execute(requestCurrent, new ForecastWeatherRequestListener());

        CurrentWeatherRequest requestForecast = new CurrentWeatherRequest(latitude, longitude, apiKey);
        spiceManager.execute(requestForecast, new CurrentWeatherRequestListener());
    }

    private class ForecastWeatherRequestListener implements RequestListener<ForecastWeatherModel> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
        }

        @Override
        public void onRequestSuccess(ForecastWeatherModel forecastWeatherModel) {
            textViewForecast.setVisibility(View.VISIBLE);


        }
    }

        private class CurrentWeatherRequestListener implements RequestListener<CurrentWeatherModel> {



            @Override
            public void onRequestFailure(SpiceException e) {
                //update your UI
            }


        @Override
        public void onRequestSuccess(CurrentWeatherModel currentWeatherModel) {

            int windSpeed = (int) (currentWeatherModel.getWind().getSpeed() * 3.6);

            if (currentWeatherModel.getName().length() > 0) {
                textViewCurrent.setText("Ville : " + "\n" + "Vitesse du vent : " + windSpeed + " Km/h");

            }

        }

    }

}




