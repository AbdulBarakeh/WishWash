package app.project.wishwash.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.project.wishwash.R;
import app.project.wishwash.models.Message;

import static app.project.wishwash.base.BaseApplication.CHANNEL_MESSAGE;

public class WishWashService extends Service {
    private IBinder binder = new serviceBinder();
    private static String MESSAGE_NOTIFICATION ="notificationMessage";
    private static final String TAG = "WishWashService";
    NotificationManager notificationManager;
    Notification messageNotification;
    NotificationChannel notificationChannel;




    public WishWashService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
                            messageNotification = new NotificationCompat.Builder(WishWashService.this,MESSAGE_NOTIFICATION)
                                    .setSmallIcon(R.drawable.guest_24dp)
                                    .setContentTitle("New Message!")
                                    .setContentText(currentMessage.getSender().getUserName() +" sent you a message!")
                                    .build();
                            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(666,messageNotification);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
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

