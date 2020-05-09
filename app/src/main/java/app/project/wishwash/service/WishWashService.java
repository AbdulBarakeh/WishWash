package app.project.wishwash.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.project.wishwash.R;
import app.project.wishwash.models.Message;

import static app.project.wishwash.base.BaseApplication.MESSAGE_CHANNEL;
import static app.project.wishwash.base.BaseApplication.WELCOME_CHANNEL;

public class WishWashService extends Service {
    private IBinder binder = new serviceBinder();
    private static int MESSAGE_NOTIFICATION = 666;
    private static int WELCOME_NOTIFICATION = 667;
    FirebaseUser firebaseUser;
    NotificationManager notificationManager;
    NotificationCompat.Builder messageNotification;
    NotificationCompat.Builder welcomeNotification;
    Context context;


    public WishWashService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        SendWelcomeNotification();
        NotifyOnMessageReceive();

    }

    private void NotifyOnMessageReceive() {
        DatabaseReference messageDBRef = FirebaseDatabase.getInstance().getReference("messages");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            messageDBRef.orderByChild("messageDate").limitToLast(1).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()){
                        Message currentMessage = snap.getValue(Message.class);
                        if (currentMessage.getReceiver().getUserId().equals(user.getUid()) ){
                            messageNotification = new NotificationCompat.Builder(context, MESSAGE_CHANNEL)
                                    .setSmallIcon(R.drawable.app_icon)
                                    .setContentTitle("New Message!")
                                    .setContentText(currentMessage.getSender().getUserName() +" sent you a message!");

                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(MESSAGE_NOTIFICATION,messageNotification.build());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }
//Inspiration from this source: https://developer.android.com/guide/components/processes-and-threads
public void SendWelcomeNotification(){
    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            welcomeNotification = new NotificationCompat.Builder(context, WELCOME_CHANNEL)
                    .setSmallIcon(R.drawable.app_icon) // Change image
                    .setContentTitle(getString(R.string.welcome_notification_title) + " " + firebaseUser.getDisplayName() + "!")
                    .setContentText(getString(R.string.welcome_notification_content));

            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(WELCOME_NOTIFICATION, welcomeNotification.build());
        }
    }).start();
}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public class serviceBinder extends Binder{
        public WishWashService getService(){
            return WishWashService.this;
        }
    }


}

