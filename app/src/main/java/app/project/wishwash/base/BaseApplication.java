package app.project.wishwash.base;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import app.project.wishwash.R;

public class BaseApplication extends Application {
    public static final String SERVICE_CHANNEL = "Service_Channel";
    private static final String TAG = "BaseApplication";
    NotificationManager notificationManager;
    public static final String WELCOME_CHANNEL = "Welcome_Channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }
    //Creating notification channelse for API versions over android OREO
    private void createNotificationChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){

            NotificationChannel serviceChannel = new NotificationChannel(SERVICE_CHANNEL,"Service_Channel", NotificationManager.IMPORTANCE_HIGH);
            serviceChannel.setDescription("Hahahha");

            NotificationChannel welcomeChannel = new NotificationChannel(WELCOME_CHANNEL,"Welcome_Channel", NotificationManager.IMPORTANCE_HIGH);
            welcomeChannel.setDescription("welcome message notification");


            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(serviceChannel);
            notificationManager.createNotificationChannel(welcomeChannel);
        }
        Log.d(TAG , "createNotificationChannel: notification channels created ");
    }
}
