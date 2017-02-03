package com.herprogramacion.crunch_expenses;

import android.content.Intent;
import android.app.IntentService;
import android.util.Log;

public class MySyncService extends IntentService{

    private static final String TAG = "com.herprogramacio";

    public MySyncService() {
        super("MySyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Esto es lo que hace el servicio
        Log.i(TAG, "Servicio Iniciado");
    }
}
