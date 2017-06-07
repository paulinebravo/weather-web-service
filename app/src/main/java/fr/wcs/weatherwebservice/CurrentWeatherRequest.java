package fr.wcs.weatherwebservice;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.gson.GsonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import java.util.Locale;

import roboguice.util.temp.Ln;

/**
 * Created by apprenti on 30/05/17.
 */

public class CurrentWeatherRequest extends GoogleHttpClientSpiceRequest<CurrentWeatherModel> {
    private String baseUrl;

    public CurrentWeatherRequest(double latitude, double longitude, String apiKey) {
        super(CurrentWeatherModel.class);
        this.baseUrl = String.format("http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid="+apiKey+"&lang=fr");
    }

    @Override
    public CurrentWeatherModel loadDataFromNetwork() throws Exception {
        Ln.d("Call web service " + baseUrl);
        HttpRequest request = getHttpRequestFactory()//
                .buildGetRequest(new GenericUrl(baseUrl));
        request.setParser(new GsonFactory().createJsonObjectParser());
        return request.execute().parseAs(getResultType());
    }

    public String createCacheKey() {
        return "Weather." + baseUrl;
    }
}
