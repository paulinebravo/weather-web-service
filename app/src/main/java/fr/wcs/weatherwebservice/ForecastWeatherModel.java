package fr.wcs.weatherwebservice;

import java.util.ArrayList;
import com.google.api.client.util.Key;


public class ForecastWeatherModel {

    @Key
    private String cod;
    @Key
    private Double message;
    @Key
    private Integer cnt;
    @Key
    private java.util.List< fr.wcs.weatherwebservice.List> list = new ArrayList< fr.wcs.weatherwebservice.List>();
    @Key
    private City city;


    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Double getMessage() {
        return message;
    }

    public void setMessage(Double message) {
        this.message = message;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public java.util.List< fr.wcs.weatherwebservice.List> getList() {
        return list;
    }

    public void setList(java.util.List< fr.wcs.weatherwebservice.List> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

}

