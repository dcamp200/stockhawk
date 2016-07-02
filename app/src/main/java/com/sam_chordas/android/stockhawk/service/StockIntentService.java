package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.TaskParams;
import com.sam_chordas.android.stockhawk.ui.Constants;

/**
 * Created by sam_chordas on 10/1/15.
 */
public class StockIntentService extends IntentService {
    private static final String LOG_TAG = StockIntentService.class.getSimpleName();

    public StockIntentService() {
        super(StockIntentService.class.getName());
    }

    public StockIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(StockIntentService.class.getSimpleName(), "Stock Intent Service onHandleIntent");
        StockTaskService stockTaskService = new StockTaskService(this);
        Bundle args = new Bundle();
        if (intent.getStringExtra("tag").equals("add")) {
            args.putString("symbol", intent.getStringExtra("symbol"));
        }
        // We can call OnRunTask from the intent service to force it to run immediately instead of
        // scheduling a task.
        try {
            stockTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));
        } catch (IllegalArgumentException iae) {
            Log.d(LOG_TAG, "Stock " + intent.getStringExtra("symbol") + " not found");
            Intent localIntent =
                    new Intent(Constants.SYMBOL_LOOKUP_FAILURE)
                            // Puts the status into the Intent
                            .putExtra(Constants.SYMBOL_NOT_FOUND, intent.getStringExtra("symbol"));
            // Broadcasts the Intent to receivers in this app.
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        }
    }
}
