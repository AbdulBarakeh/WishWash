package app.project.wishwash.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

import androidx.core.app.NotificationCompat;
import app.project.wishwash.R;

public class WishWashService extends Service {
    private IBinder binder = new serviceBinder();
    private static final String TAG = "WishWashService";
    private Thread thread;
    private Context context = this;
    FirebaseUser firebaseUser;

    public WishWashService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        thread = new Thread(new WelcomeRunnable());
        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("WishWashService", "Service started");

        return START_NOT_STICKY;
    }

    /*
     * BINDER
     */
    public class serviceBinder extends Binder {
        public WishWashService getService() {
            return WishWashService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: bound!");
        return binder;
    }


    private class WelcomeRunnable implements Runnable {
        @Override
        public void run() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            androidx.core.app.NotificationCompat.Builder builder = new androidx.core.app.NotificationCompat.Builder(context, "1")
                    .setSmallIcon(R.drawable.icon_calendar) // Change image
                    .setContentTitle(getString(R.string.welcome_notification_title) + " " + firebaseUser.getDisplayName() + "!")
                    .setContentText(getString(R.string.welcome_notification_content))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setOngoing(false)
                    .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.notify(1, builder.build());
        }}
    };

    // Send welcome-notification when user has logged in to WishWash
    private void sendNotification() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                androidx.core.app.NotificationCompat.Builder builder = new androidx.core.app.NotificationCompat.Builder(context, "1")
                        .setSmallIcon(R.drawable.icon_calendar) // Change image
                        .setContentTitle(getString(R.string.welcome_notification_title) + firebaseUser.getDisplayName())
                        .setContentText(getString(R.string.welcome_notification_content))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setOngoing(false)
                        .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                assert notificationManager != null;
                notificationManager.notify(1, builder.build());
            }
        }, "WelcomeToWishWashThread" );

        thread.start();
    }


    // ----- Notifications -----
    /*private void createNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "WishWashChannel";
            String description = "WishWashChannel_Descriptor";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(){
        createNotification();
        androidx.core.app.NotificationCompat.Builder builder = new androidx.core.app.NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.icon_calendar)
                .setContentTitle(getString(R.string.welcome_notification_title))
                .setContentText(getString(R.string.welcome_notification_content))
                .setDefaults(Notification.DEFAULT_ALL)
                .setOngoing(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private class BlockingTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendNotification();

            return null;
        }
    }*/
}
