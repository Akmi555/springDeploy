package org.blb.DTO.weather.weatherJsonDataModel;


import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Data

public class Datum {

    public String wind_cdir;
    public int rh;
    public String pod;
    public double lon;
    public double pres;
    public String timezone;
    public String obTime;
    public String country_code;
    public int clouds;
    public int vis;
    public double wind_spd;
    public int gust;
    public String wind_cdir_full;
    public double app_temp;
    public String stateCode;
    public int ts;
    public int hAngle;
    public double dewpt;
    public Weather weather;
    public int uv;
    public int aqi;
    public String station;
    public List<String> sources = null;
    public int wind_dir;
    public int elev_angle;
    public String datetime;
    public int precip;
    public double ghi;
    public int dni;
    public int dhi;
    public int solar_rad;
    public String city_name;
    public String sunrise;
    public String sunset;
    public double temp;
    public double lat;
    public double slp;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
