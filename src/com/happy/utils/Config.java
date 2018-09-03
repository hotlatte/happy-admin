package com.happy.utils;

import java.util.Enumeration;
import java.util.ResourceBundle;

public class Config {
    private static ResourceBundle config = ResourceBundle.getBundle("config/config");

    public static String getString(String name) {
        return config.getString(name);
    }

    public static Object getObject(String name) {
        return config.getObject(name);
    }

    public static String[] getStringArray(String name) {
        return config.getStringArray(name);
    }

    public static Enumeration<String> getKeys() {
        return config.getKeys();
    }
}
