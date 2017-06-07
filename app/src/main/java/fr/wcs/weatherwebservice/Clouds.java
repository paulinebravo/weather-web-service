
package fr.wcs.weatherwebservice;


import com.google.api.client.util.Key;

public class Clouds {

    @Key
   private Integer all;

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }

}