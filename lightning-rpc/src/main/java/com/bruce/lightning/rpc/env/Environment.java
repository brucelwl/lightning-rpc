package com.bruce.lightning.rpc.env;


public interface Environment {


    boolean containsProperty(String key);

    String getProperty(String key);

    String getProperty(String key, String def);


}
