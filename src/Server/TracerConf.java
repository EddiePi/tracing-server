package Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eddie on 2017/3/9.
 */
public class TracerConf {

    private static final TracerConf instance = new TracerConf();

    public static TracerConf getInstance() {
        return instance;
    }

    Map<String, String> setting;

    private TracerConf(){
        setting = new HashMap<>();
        getConfFromFile();
    }

    private void getConfFromFile() {
        String path = "tracer.conf";
        try {
            List<String> strings = Utils.FileReader.read(path);
            for(String str: strings) {
                String[] result = str.split("\\s+");
                setting.put(result[0], result[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getIntegerOrDefault(String key, Integer defaultValue) {
        String valueStr = setting.get(key);
        if (valueStr == null) {
            return defaultValue;
        }
        Integer value;
        try {
            value = Integer.valueOf(valueStr);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
        return value;
    }

    public String getStringOrDefault(String key, String defaultValue) {
        String valueStr = setting.get(key);
        return valueStr != null ? valueStr : defaultValue;
    }

    public Double getDoubleOrDefault(String key, Double defaultValue) {
        String valueStr = setting.get(key);
        if (valueStr == null) {
            return defaultValue;
        }
        Double value;
        try {
            value = Double.valueOf(valueStr);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
        return value;
    }
}
