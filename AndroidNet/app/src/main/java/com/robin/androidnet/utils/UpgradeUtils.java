package com.robin.androidnet.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by robin on 2016/8/10.
 */
public class UpgradeUtils {
    public static String packageName="com.robin.androidnet";
    public static int getVerCode(Context context){
        int verCode=-1;
        try {
            verCode=context.getPackageManager().getPackageInfo(packageName,0).versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }
    public static String getVerName(Context context){
        String verName=null;
        try {
            verName=context.getPackageManager().getPackageInfo(packageName,0).versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }


}
