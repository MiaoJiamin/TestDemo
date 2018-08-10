//    写一条基础类，所有case继承该类，该类满足以下功能：
//            1. 可以记录测试步骤并保存在指定文件；
//            2. 可以记录失败时设备的截图；
//            3. 测试结束后获取当条case执行的所有log
package com.example.miaojiamin.demo;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    public UiDevice mdevice;
    public Instrumentation instrumentation;
    public String LogFileName;      //log文件的名字,按时间命名
    public String path = "/sdcard/Android/data/com.example.miaojiamin.demo/cache/";

    //将当前case的所有log保存到.log文件中
    public void catchLog(){
        try {
            mdevice.executeShellCommand("logcat -d -f " + path + LogFileName + ".log");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //将当前case的测试步骤保存到.txt文件中
    public void myLog(String in) {
        FileWriter fw = null;
        try {
            File f = new File(path + LogFileName + ".txt");
            if(!f.exists())
                f.createNewFile();
            fw = new FileWriter(f, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            PrintWriter pw = new PrintWriter(fw);
            pw.println(in);
            pw.flush();
            fw.flush();
            pw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //保存失败时的截图
    public void ScreenShot(){
        File file = new File(path + LogFileName + ".png");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mdevice.takeScreenshot(file,1.0f,10);
        mdevice.waitForIdle();
    }

    //运算单元
    public void ALU(String srcop,int src){
        String op = "";
        if(srcop.equals("+"))
            op = "com.miui.calculator:id/btn_plus_s";
        else if(srcop.equals("-"))
            op = "com.miui.calculator:id/btn_minus_s";
        else if(srcop.equals("*"))
            op = "com.miui.calculator:id/btn_mul_s";
        else if(srcop.equals("/"))
            op = "com.miui.calculator:id/btn_div_s";
        try {
            UiObject objectop = mdevice.findObject(new UiSelector().resourceId(op));
            objectop.click();
            myLog("click " +  srcop);
        } catch (UiObjectNotFoundException e) {
            ScreenShot();
            e.printStackTrace();
        }
        clicknum(src);
    }

    public void clicknum(int src){
        int num = 0;
        ArrayList<Integer> list = new ArrayList<Integer>();
        do{
            num = src % 10;
            list.add(num);
            src = src / 10;
        }while(src != 0);

        for(int i = list.size() - 1; i >= 0; i--){
            try {
                UiObject object = mdevice.findObject(new UiSelector().className("android.widget.Button").text(String.valueOf(list.get(i))));
                object.click();
                myLog("click " + String.valueOf(list.get(i)));
            } catch (UiObjectNotFoundException e) {
                ScreenShot();
                e.printStackTrace();
            }
        }
    }

    @Before
    public void setUp(){
        instrumentation = InstrumentationRegistry.getInstrumentation();
        mdevice = UiDevice.getInstance(instrumentation);
        LogFileName = new SimpleDateFormat("HH.mm.ss-yyyyMMdd").format(new Date());//按时间命名
        try {
            mdevice.executeShellCommand("logcat -c");
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Test
    public void useAppContext() {
        try {
            myLog("--------------- case begin ----------------");
            mdevice.wait(Until.findObject(By.text("计算器")), 2000);
            UiObject2 uiObject2 = mdevice.findObject(By.text("计算器"));
            uiObject2.click();
            myLog("open calculator");

            UiObject obclear = mdevice.findObject(new UiSelector().resourceId("com.miui.calculator:id/btn_c_s"));
            UiObject obequal = mdevice.findObject(new UiSelector().resourceId("com.miui.calculator:id/btn_equal_s"));
            obclear.click();
            clicknum(66);
            ALU("+",5);
            ALU("-",1);
            ALU("/",6);
            ALU("*",2);
            obequal.click();
            myLog("--------------- case end -------------------");
        }catch(Exception e){
            ScreenShot();
            e.printStackTrace();
        }
    }


    @After
    public void after(){
        catchLog();
        mdevice.pressHome();
    }

}
