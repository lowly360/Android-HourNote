package com.itlowly.twenty.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 服务的工具类
 * @author lowly_pc
 *
 */
public class ServiceUtils {

	/**
	 * 检测特定服务是否在运行
	 * @param context
	 * @param serviceClassName
	 * @return
	 */
	public static boolean isServiceRunning(Context context,String serviceClassName){ 
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE); 
        final List<RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE); 
 
        for (RunningServiceInfo runningServiceInfo : services) { 
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)){ 
                return true; 
            } 
        } 
        return false; 
     }
}
