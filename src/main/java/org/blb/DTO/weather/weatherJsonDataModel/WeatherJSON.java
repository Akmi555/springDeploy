package org.blb.DTO.weather.weatherJsonDataModel;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data

public class WeatherJSON {

    public List<Datum> data = null;
    public List<Object> minutely = null;
    public int count;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


}
