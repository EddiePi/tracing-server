package JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by Eddie on 2017/5/18.
 */
public class JsonWriter {
    String path;
    String metricName;
    JSONArray topJsonArray;
    JSONObject topJsonObject;
    JSONArray dataArray;

    public void setNewMetric(String newPath, String metricName) {
        this.path = newPath;
        this.metricName = metricName;
        topJsonObject = new JSONObject();
        topJsonArray = new JSONArray();
        try {
            topJsonObject.put("target", metricName);
            dataArray = new JSONArray();
            topJsonObject.put("datapoints", dataArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        topJsonArray.put(topJsonObject);

    }

    public JsonWriter() {}

    public JsonWriter(String path, String metricName) {
        setNewMetric(path, metricName);
    }

    public JSONObject getTopJsonObject() {
        return topJsonObject;
    }

    public String getMetricName() {
        String name = null;
        try {
            name =  topJsonObject.getString("target");
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return name;
        }
    }


    public void writeDataPoints(List<Double> metricValue, List<Long> timestamp) {
        for(int i = 0; i < metricValue.size(); i++) {
            JSONArray oneData = new JSONArray();
            oneData.put(metricValue.get(i));
            oneData.put(timestamp.get(i));
            dataArray.put(oneData);
        }
        try {
            writeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //把json格式的字符串写到文件
    private void writeFile() throws IOException {
        FileWriter fw = new FileWriter(path);
        PrintWriter out = new PrintWriter(fw);
        out.write(topJsonArray.toString());
        out.println();
        fw.close();
        out.close();
    }
}
