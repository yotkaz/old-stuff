package me.yotkaz.computersimulations.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by yotkaz on 2014-11-11.
 */
public class MainConfig {
    private Properties configProperties = new Properties();

    public MainConfig() throws IOException {
        final String CONFIG_FILE = "config.properties";
        InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
        configProperties.load(input);
    }

    public Properties getConfigProperties() {
        return configProperties;
    }
}
