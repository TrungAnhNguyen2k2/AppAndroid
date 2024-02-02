package com.example.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.accessibility.AccessibilityEvent;
import android.util.Log;
import java.time.LocalDateTime;


public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG="MyAccessibilityService";
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
//        Log.d(TAG, "onAccessibilityEvent");
        String packageName= event.getPackageName().toString();
        PackageManager packageManager= this.getPackageManager();
        LocalDateTime currentTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentTime = LocalDateTime.now();
        }
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName,0);
            CharSequence applicationLabel = packageManager.getApplicationLabel(applicationInfo);
            Log.i(TAG, currentTime+ " APP:"+ applicationLabel);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onInterrupt() {
        Log.e(TAG,"INTERUPT");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED|
                AccessibilityEvent.TYPE_VIEW_FOCUSED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 100;
        this.setServiceInfo(info);
        Log.d(TAG,"onServiceConnected:");
    }
}
