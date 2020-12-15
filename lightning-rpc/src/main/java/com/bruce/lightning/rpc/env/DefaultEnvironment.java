package com.bruce.lightning.rpc.env;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class DefaultEnvironment implements Environment {

    private static volatile DefaultEnvironment defaultEnvironment;

    private static final HashMap<String, String> source = new HashMap<>();

    private DefaultEnvironment() {

    }

    public static DefaultEnvironment getInstance() {
        if (defaultEnvironment == null) {
            synchronized (DefaultEnvironment.class) {
                if (defaultEnvironment == null) {
                    defaultEnvironment = new DefaultEnvironment();
                    Properties properties = new Properties();
                    InputStream in = DefaultEnvironment.class.getClassLoader().getResourceAsStream("lightning-rpc.properties");
                    try {
                        properties.load(in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    properties.forEach((k, v) -> source.put(k.toString(), v.toString()));
                }
            }
        }
        return defaultEnvironment;
    }


    @Override
    public boolean containsProperty(String key) {
        return source.containsKey(key);
    }

    @Override
    public String getProperty(String key) {
        return source.get(key);
    }

    @Override
    public String getProperty(String key, String def) {
        return source.getOrDefault(key, def);
    }

}
