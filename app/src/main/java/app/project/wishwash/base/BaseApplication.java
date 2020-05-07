package app.project.wishwash.base;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import app.project.wishwash.R;

public class BaseApplication extends Application {
    public static String CHANNEL_MESSAGE = "channelMessage";
    private static final String TAG = "BaseApplication";
    NotificationManager notificationManager;
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }
    //Creating notification channelse for API versions over android OREO
    private void createNotificationChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_MESSAGE,"MessageNoti", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Notification Channel for messages");
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Log.d(TAG , "createNotificationChannel: notification channels created ");
    }
}
