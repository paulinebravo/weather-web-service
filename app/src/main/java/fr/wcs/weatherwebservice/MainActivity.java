package fr.wcs.weatherwebservice;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.GsonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int MY_REQUEST_FOR_LOCATION = 1;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude;
    private double longitude;
    private String apiKey;
    protected SpiceManager spiceManager = new SpiceManager(GsonGoogleHttpClientSpiceService.class);
    TextView textViewWind;
    TextView textViewCity;
    TextView textViewCloud;
    TextView textViewPressure;
    ListView listViewForecast;
    private WeatherAdapter weatherAdapter;

    // Acquire a reference to the system Location Manager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewWind = (TextView)findViewById(R.id.textViewWind);
        textViewCity = (TextView) findViewById(R.id.textViewCity);
        textViewCloud= (TextView) findViewById(R.id.textViewCloud);
        textViewPressure= (TextView)findViewById(R.id.textViewPressure);
        listViewForecast= (ListView) findViewById(R.id.listViewForecast);

        apiKey = getString(R.string.apiKey);




        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                latitude = location.getLatitude();
                longitude = location.getLongitude();
                performRequest();
                // Called when a new location is found by the network location provider.
                Toast.makeText(MainActivity.this, "lat : " + location.getLatitude() + " " + "lon : " + location.getLongitude() , Toast.LENGTH_SHORT).show();            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_REQUEST_FOR_LOCATION);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        spiceManager.shouldStop();
        locationManager.removeUpdates(locationListener);
    }
    private void performRequest() {
        MainActivity.this.setProgressBarIndeterminateVisibility(true);

        ForecastWeatherRequest requestForecast = new ForecastWeatherRequest(latitude, longitude, apiKey);
        spiceManager.execute(requestForecast, new ForecastWeatherRequestListener());

        CurrentWeatherRequest requestCurrent = new CurrentWeatherRequest(latitude, longitude, apiKey);
        spiceManager.execute(requestCurrent, new CurrentWeatherRequestListener());

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }

    private class ForecastWeatherRequestListener implements RequestListener<ForecastWeatherModel> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {

        }

        @Override
        public void onRequestSuccess(ForecastWeatherModel forecastWeatherModel) {


            final ArrayList<List> weatherList = (ArrayList<List>) forecastWeatherModel.getList();
            final WeatherAdapter weatherAdapter = new WeatherAdapter(MainActivity.this, weatherList);
            listViewForecast.setAdapter(weatherAdapter);

        }
    }


    private class CurrentWeatherRequestListener implements RequestListener<CurrentWeatherModel> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {


        }

        @Override
        public void onRequestSuccess(CurrentWeatherModel currentWeatherModel) {
            double windSpeed = currentWeatherModel.getWind().getSpeed()*3.6;
            textViewWind.setText(String.valueOf(windSpeed));
            textViewCity.setText(currentWeatherModel.getName());
            double temperatureC = currentWeatherModel.getMain().getTemp()-273.15;
            textViewCloud.setText(String.valueOf(temperatureC));
            textViewPressure.setText(String.valueOf(currentWeatherModel.getMain().getPressure()));



        }
    }
}
