package com.herprogramacion.crunch_expenses;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.herprogramacion.crunch_expenses.sync.SyncAdapter;

public class MiServicio extends Service {

    private final String TAG = "SERVICETAG";
    private Handler mHandler;

    public MiServicio() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler.postDelayed(ToastTask, 10000); // Starts the loop here
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    private Runnable ToastTask = new Runnable() {
        @Override
        public void run() {
            Log.i("update", "Intentando actualizar");
            try{
            SyncAdapter.sincronizarAhora(getApplicationContext(), false);
            // Schedule this Runnable to run again after 10 sec
            mHandler.postDelayed(this, 10000);
            }catch (Exception e){
                Log.i("Excepcion lanzada", e.getMessage());
            }

        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Servicio Destruido");
    }
}
