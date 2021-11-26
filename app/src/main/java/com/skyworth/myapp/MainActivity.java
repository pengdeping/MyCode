package com.skyworth.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemProperties;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("pdp", "bootloader.version = " + getAndroidOsSystemProperties("ro.boot.skyworthdigital.bootloader.version"));
        Log.d("pdp", "ro.oem.key1 : " + getAndroidOsSystemProperties("ro.oem.key1"));
    }

    static String getAndroidOsSystemProperties(String key) {
        String ret;
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Method systemProperties_get = systemProperties.getMethod("get",String.class);
            if ((ret = (String) systemProperties_get.invoke(null, key)) != null)  return ret;
        } catch (Exception e) {
            return null;
        }
        return "";
    }

    String get(String key){
        String ret;
        try {
            Class<?> systemProp = Class.forName("android.os.SystemProperties");
            Method get = systemProp.getMethod("get", String.class);
            if ((ret= (String) get.invoke(null, key)) != null) return ret;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}