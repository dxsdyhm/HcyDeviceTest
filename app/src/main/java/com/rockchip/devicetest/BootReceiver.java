/*******************************************************************
* Company:     Fuzhou Rockchip Electronics Co., Ltd
* Filename:    BootReceiver.java  
* Description:   
* @author:     fxw@rock-chips.com
* Create at:   2012-4-23 上午09:13:38  
* 
* Modification History:  
* Date         Author      Version     Description  
* ------------------------------------------------------------------  
* 2012-4-6      fxw         1.0         create
*******************************************************************/   
package com.rockchip.devicetest;

import static com.rockchip.devicetest.service.TestService.FILE_FACTORY_TEST;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.rockchip.devicetest.service.TestService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageParser;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	private Context mContext;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		String action = intent.getAction();
		if(Intent.ACTION_MEDIA_MOUNTED.equals(action)){
			Intent newIntent = new Intent(mContext, TestService.class);
			String path = intent.getDataString();
			if(path!=null&&path.endsWith("/mnt/sdcard")){
				newIntent.putExtra(TestService.EXTRA_KEY_TESTFROM, "app");
			}else{
				newIntent.putExtra(TestService.EXTRA_KEY_TESTFROM, "mount");
				if(!path.contains("storage/emulated/0")){
					String upath = intent.getDataString().replace("file://","");
					newIntent.putExtra(TestService.EXTRA_KEY_UPATH,upath);
				}
			}
			mContext.startService(newIntent);
			if(SystemProperties.getInt("persist.sys.skipservice",0)!=0&&isInFactoryTest(context)){
				newIntent.setClass(context,IndexActivity.class);
				ActivityUtils.startActivity(newIntent);
			}
		}else if(Intent.ACTION_BOOT_COMPLETED.equals(action)){
			Intent newIntent = new Intent(mContext, TestService.class);
			newIntent.putExtra(TestService.EXTRA_KEY_TESTFROM, "boot");
			mContext.startService(newIntent);
		}
	}

	private boolean isInFactoryTest(Context context) {
		return ConfigFinder.hasConfigFile(FILE_FACTORY_TEST, context);
	}
}
