package fr.wcs.weatherwebservice;
import com.google.api.client.util.Key;

public class Wind {
    @Key
    private Double speed;
    @Key
    private Double deg;

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getDeg() {
        return deg;
    }

    public void setDeg(Double deg) {
        this.deg = deg;
    }

}