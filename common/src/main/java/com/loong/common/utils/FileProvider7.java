package com.loong.common.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;

public class FileProvider7 {

    public static Uri getUriForFile(Context context, File file){
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            Log.d("getUriForFile",">=Build.VERSION_CODES.N");
            fileUri = FileProvider.getUriForFile(context,context.getPackageName()+".android7.fileprovider",file);
        }else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }
}
