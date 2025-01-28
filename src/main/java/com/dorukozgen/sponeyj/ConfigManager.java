package com.dorukozgen.sponeyj;

import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private final Properties properties = new Properties();

    public ConfigManager(String path) throws IOException {
        this.properties.load(getClass().getResourceAsStream(path));
    }

    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }
}
