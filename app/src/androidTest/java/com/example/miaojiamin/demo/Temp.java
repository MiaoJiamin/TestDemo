
package com.example.miaojiamin.demo;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RunWith(AndroidJUnit4.class)
public class Temp {

    @Test
    public void test(){
        try {
            Runtime.getRuntime().exec("logcat -d > /sdcard/a/log3.txt");
            Runtime.getRuntime().exec("/system/bin/logcat -d > /sdcard/a/log3.txt");



            Process p = Runtime.getRuntime().exec("mkdir /sdcard/0000");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
