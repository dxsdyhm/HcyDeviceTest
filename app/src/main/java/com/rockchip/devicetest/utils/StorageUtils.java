package com.rockchip.devicetest.utils;

import java.io.*; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.reflect.Method;

import android.os.Build;
import android.util.Log;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.os.storage.DiskInfo;

import com.rockchip.devicetest.BuildConfig;

/** 
 *StorageUtils is use to get correct storage path between ics and gingerbread.
 *add by lijiehong
 */ 
public class StorageUtils { 
    public static String TAG = "StorageUtils";
    public static String mUsbDirs;
    public static String mSDcardDir;

    
    /*
     * ?D??��a??SD?����?��?1��??
     */
    public static boolean isMountSD(StorageManager storageManager) {

        final List<VolumeInfo> volumes = storageManager.getVolumes();
        //Collections.sort(volumes, VolumeInfo.getDescriptionComparator());

        for (VolumeInfo vol : volumes) {
            if (vol.getType() == VolumeInfo.TYPE_PUBLIC) {
                DiskInfo disk = vol.getDisk();
                if (disk != null && disk.isSd()) {
                    // sdcard dir
                    int status = vol.getState();
                    Log.d(TAG,"isMountSD()--status-->" + status);
                    if (status == VolumeInfo.STATE_MOUNTED) {
                        return true;
                    }
                    //continue;
                }
            }
        }
        return false;
    }
   
    /*
     * ??��?��a??SD?��1��??????
     */
    public static String getSDcardDir(StorageManager storageManager) {

        final List<VolumeInfo> volumes = storageManager.getVolumes();
        //Collections.sort(volumes, VolumeInfo.getDescriptionComparator());
        for (VolumeInfo vol : volumes) {
            if (vol.getType() == VolumeInfo.TYPE_PUBLIC) {
                DiskInfo disk = vol.getDisk();
                if (disk != null) {
                    if (disk.isSd()) {
                        //mSDcardDir = vol.path;
                        mSDcardDir = vol.internalPath;
                        Log.d(TAG,"hjc getSDcardDir()--mSDcardDir-->" + mSDcardDir); 
                    }
                }
            }
        }
		
		if (null != mSDcardDir) {
            /*int end = mSDcardDir.lastIndexOf('/');
            if (end > 0)    // case mSDcardDir = /xxx/xxx
                return mSDcardDir.substring(0, end);
            else            // case mSDcardDir = /xxx*/
                return mSDcardDir;
			//return mSDcardDir;
        } else {
            return null;
        }
 
    }
    
    /**
     * 获取所有SD卡路径
     * @param storageManager
     * @return
     */
    public static List<String> getSdCardPaths(StorageManager storageManager){
    	List<String> sdPaths = new ArrayList<String>();
        final List<VolumeInfo> volumes = storageManager.getVolumes();
        //Collections.sort(volumes, VolumeInfo.getDescriptionComparator());
        for (VolumeInfo vol : volumes) {
            if (vol.getType() == VolumeInfo.TYPE_PUBLIC) {
                DiskInfo disk = vol.getDisk();
                if (disk != null) {
                    if (disk.isSd()) {
                        //sdPaths.add(vol.path);
                        sdPaths.add(vol.internalPath);
                    }
                }
            }
        }
		
		return sdPaths;
    
    }
    
    
    /**
     * 获取所有ＵＳＢ路径
     * @param storageManager
     * @return
     */
    public static List<String> getUsbPaths(StorageManager storageManager){

    	List<String> usbPaths = new ArrayList<String>();
        final List<VolumeInfo> volumes = storageManager.getVolumes();
        //Collections.sort(volumes, VolumeInfo.getDescriptionComparator());
        for (VolumeInfo vol : volumes) {
            if (vol.getType() == VolumeInfo.TYPE_PUBLIC) {
                DiskInfo disk = vol.getDisk();
                if (disk != null) {
                    if (disk.isUsb()) {
                        // TODO: 2020/5/6 本地测试使用path,正式环境改为internalPath
                        if(Build.VERSION.SDK_INT >= 30){
                            usbPaths.add(vol.path);
                        }else {
                            usbPaths.add(vol.internalPath);
                        }
                    }
                }
            }
        }
		
		return usbPaths;
    
    }
    
    
    /*
     * ?D??��?��?��DUSB������?1��??
     */
    public static boolean isMountUSB(StorageManager storageManager) {

        final List<VolumeInfo> volumes = storageManager.getVolumes();
        Collections.sort(volumes, VolumeInfo.getDescriptionComparator());

        for (VolumeInfo vol : volumes) {
            if (vol.getType() == VolumeInfo.TYPE_PUBLIC) {
                DiskInfo disk = vol.getDisk();
                if (disk != null) {
                    if (disk.isUsb()) {
                        // usb dir
                        int status = vol.getState();
                        Log.d(TAG,"isMountUSB()--status-->" + status);
                        if (status == VolumeInfo.STATE_MOUNTED) {
                            return true;
                        }
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /*
     * ??��?USB1��????????
     */
    public static String getUsbDir(StorageManager storageManager) {

        final List<VolumeInfo> volumes = storageManager.getVolumes();
        //Collections.sort(volumes, VolumeInfo.getDescriptionComparator());
        for (VolumeInfo vol : volumes) {
            if (vol.getType() == VolumeInfo.TYPE_PUBLIC) {
                Log.d("YHX", "VolumeInfo.TYPE_PUBLIC");
                Log.d("YHX", "Volume path:" + vol.getPath());
                DiskInfo disk = vol.getDisk();
                if (disk != null) {
                    if (disk.isUsb()) {
                        // usb dir
//                        StorageVolume sv = vol.buildStorageVolume(context, context.getUserId(),
//                                false);
                        mUsbDirs = vol.path;
                    }
                }
            }
        }
        
        if (null != mUsbDirs) {
          int end = mUsbDirs.lastIndexOf('/');
            if (end > 0)    // case mUsbDirs = /xxx/xxx
                return mUsbDirs.substring(0, end);
            else            // case mUsbDirs = /xxx
                return mUsbDirs;
        	//return mUsbDirs;
        } else {
            return null;
        }
    }

    public static Object invokeStaticMethod(Class<?> cls, String methodName, Object... arguments) {
        try {
            Class<?>[] parameterTypes = null;
            if(arguments!=null){
                parameterTypes = new Class<?>[arguments.length];
                for(int i=0; i<arguments.length; i++){
                    parameterTypes[i] = arguments[i].getClass();
                }
            }
            Method method = cls.getMethod(methodName, parameterTypes);
            return method.invoke(null, arguments);
        }catch (Exception ex) {
            Log.d(TAG,"Invoke method error. " + ex.getMessage());
            //handleReflectionException(ex);
            return null;
        }
    }

    public static Object invokeStaticMethod(String className, String methodName, Object... arguments) {
        try {
            Class<?> cls = Class.forName(className);
            return invokeStaticMethod(cls, methodName, arguments);
        }catch (Exception ex) {
            Log.d(TAG,"Invoke method error. " + ex.getMessage());
            //handleReflectionException(ex);
            return null;
        }
    }

    public static Object invokeStaticMethod(String className, String methodName, Class<?>[] types , Object... arguments) {
        try {
            Class<?> cls = Class.forName(className);
            return invokeStaticMethod(cls, methodName, types, arguments);
        }catch (Exception ex) {
            Log.d(TAG,"Invoke method error. " + ex.getMessage());
            //handleReflectionException(ex);
            return null;
        }
    }

    public static Object invokeStaticMethod(Class<?> cls, String methodName, Class<?>[] types, Object... arguments) {
        try {
            Method method = cls.getMethod(methodName, types);
            return method.invoke(null, arguments);
        }catch (Exception ex) {
            Log.d(TAG,"Invoke method error. " + ex.getMessage());
            //handleReflectionException(ex);
            return null;
        }
    }

} 


 

