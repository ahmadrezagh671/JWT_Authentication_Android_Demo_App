package com.ahmadrezagh671.authdemoapp.Utilities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import com.ahmadrezagh671.authdemoapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppUtil {

    public static int getAppVersionCode(Context context){
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return (int) (context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES).getLongVersionCode());
            }else {
                return context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES).versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }
    public static String getAppVersionStr(Context context){
        try{
            return context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "error";
        }
    }

    public static void openLink(String url, Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Unable to open link.", Toast.LENGTH_SHORT).show();
        }
    }
    public static void sendEmail(Context context) {
        String email = context.getResources().getString(R.string.supportEmail);
        String subject = getAppName(context)+" Android App - Support";
        String body = "Hello, I need help with...";

        String mailto = "mailto:" + email +
                "?subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(body);

        Intent emailIntent = new Intent(Intent.ACTION_VIEW);
        emailIntent.setData(Uri.parse(mailto));

        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
        } catch (Exception e) {
            Toast.makeText(context, "No email apps installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getAppName(Context context){
        return context.getResources().getString(R.string.app_name);
    }

    public static String getDeviceModel(){
        return Build.MODEL;
    }

    public static String convertTimestampToString(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()); //"yyyy-MM-dd HH:mm:ss"
        return sdf.format(new Date(timestamp));
    }
}
