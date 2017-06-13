package fr.wcs.weatherwebservice;

import com.google.api.client.json.gson.GsonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import java.io.IOException;

import roboguice.util.temp.Ln;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;





import java.util.Locale;

import roboguice.util.temp.Ln;

import static android.R.attr.key;

/**
 * Created by apprenti on 30/05/17.
 */

public class ForecastWeatherRequest extends GoogleHttpClientSpiceRequest<ForecastWeatherModel> {
    private String baseUrl;

    public ForecastWeatherRequest( double latitude, double longitude, String apiKey) {
        super(ForecastWeatherModel.class );
        this.baseUrl = "http://api.openweathermap.org/data/2.5/forecast?lat="+latitude+"&lon="+longitude+"&appid="+apiKey+"&lang=fr";
    }

    @Override
    public ForecastWeatherModel loadDataFromNetwork() throws IOException {
        Ln.d( "Call web service " + baseUrl );
        ForecastWeatherModel request = getHttpRequestFactory()//
                .buildGetRequest( new GenericUrl( baseUrl ) )
                .setParser( new GsonFactory().createJsonObjectParser() )
                .execute()
                .parseAs(getResultType());
        return request;

    }


}

