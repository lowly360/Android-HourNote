package com.itlowly.twenty.utils;

import android.content.Context;

/**
 * int 转换为对应dpi的dp工具类
 * @author Administrator
 *
 */
public class DensityUtils {
	public static int dp2px(Context ctxContext,float dp) {
		float density = ctxContext.getResources().getDisplayMetrics().density;
		System.out.println("dpi = "+density);
		int px = (int) (dp*density+0.5f); //四舍五入
		
		return px;
		
	}
	
	public static float px2dp(Context ctxContext,int px){
		float density = ctxContext.getResources().getDisplayMetrics().density;
		System.out.println("dpi = "+density);
		return px/density;
	}
}
