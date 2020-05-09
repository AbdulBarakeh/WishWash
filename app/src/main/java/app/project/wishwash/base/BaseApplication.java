package app.project.wishwash.base;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

public class BaseApplication extends Application {
    public static final String MESSAGE_CHANNEL = "Message_Channel";
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

            NotificationChannel messageChannel = new NotificationChannel(MESSAGE_CHANNEL ,"message_Channel", NotificationManager.IMPORTANCE_HIGH);
            messageChannel.setDescription("Channel to show message notifications");

            NotificationChannel welcomeChannel = new NotificationChannel(WELCOME_CHANNEL,"Welcome_Channel", NotificationManager.IMPORTANCE_HIGH);
            welcomeChannel.setDescription("welcome message notification");


            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(messageChannel);
            notificationManager.createNotificationChannel(welcomeChannel);
        }
        Log.d(TAG , "createNotificationChannel: notification channels created ");
    }
}
