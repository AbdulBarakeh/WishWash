package app.project.wishwash.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class WishWashService extends Service {
    private IBinder binder = new serviceBinder();
    private static final String TAG = "WishWashService";
    public WishWashService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


/*
* BINDER
*/
    public class serviceBinder extends Binder{
        public WishWashService getService(){
            return WishWashService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG , "onBind: bound!");
        return binder;
    }

}
