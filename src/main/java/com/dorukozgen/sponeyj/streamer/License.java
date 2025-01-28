package com.dorukozgen.sponeyj.streamer;

import java.io.IOException;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class License {
    private static License instance;

    private final OkHttpClient client;

    private String key = "";

    private final String base = "localhost:1001";

    public License() {
        this.client = createClient();
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static License getInstance() {
        if (instance == null)
            instance = new License();
        return instance;
    }

    private OkHttpClient getClient() {
        return this.client;
    }

    public OkHttpClient createClient() {
        try {
            String certificateStr = "";
            return (new OkHttpClient()).newBuilder()
                    .connectTimeout(Duration.ZERO)
                    .readTimeout(Duration.ZERO)
                    .writeTimeout(Duration.ZERO)
                    .callTimeout(Duration.ZERO)

                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isSessionValid(String hwid) throws IOException {
        String json = String.format("    {\n        \"hwid\": \"%s\",\n        \"type\": \"streamer\"\n\n    }\n", new Object[] { hwid }).trim();
        System.out.println(hwid);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = (new Request.Builder()).url("http://localhost:1001/api/license/session").method("POST", requestBody).build();
        try {
            Response response = getClient().newCall(request).execute();
            Pattern pattern = Pattern.compile("\"key\":\"([^\"]+)\"");
            Matcher matcher = pattern.matcher(response.body().string());
            if (matcher.find())
                setKey(matcher.group(1));
            return (response.code() == 200);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isVaild(String key, String hwid) {
        String json = String.format("    {\n        \"key\": \"%s\",\n        \"hwid\": \"%s\",\n        \"type\": \"streamer\"\n\n    }\n", new Object[] { key, hwid }).trim();
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = (new Request.Builder()).url("http://localhost:1001/api/license/check").method("POST", requestBody).build();
        try {
            Response response = getClient().newCall(request).execute();
            if (this.key.isEmpty())
                setKey(key);
            return (response.code() == 200);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkUpdate() {
        String url = "http://localhost:1001/api/update/check?type=streamer&version=1.0.0";
        Request request = (new Request.Builder()).url(url).method("GET", null).build();
        try {
            Response response = getClient().newCall(request).execute();
            Pattern pattern = Pattern.compile("\"update\":(true|false)");
            Matcher matcher = pattern.matcher(response.body().string());
            if (matcher.find()) {
                String updateValue = matcher.group(1);
                return Boolean.parseBoolean(updateValue);
            }
            throw new RuntimeException("Update check failed");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
