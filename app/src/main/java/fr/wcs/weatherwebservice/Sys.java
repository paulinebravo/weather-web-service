
package fr.wcs.weatherwebservice;


import com.google.api.client.util.Key;

public class Sys {
    @Key
    private String pod;

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

}