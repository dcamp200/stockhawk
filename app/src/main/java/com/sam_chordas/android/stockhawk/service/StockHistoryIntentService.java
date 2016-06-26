package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.sam_chordas.android.stockhawk.ui.Constants;
import com.sam_chordas.android.stockhawk.webservices.Quote;
import com.sam_chordas.android.stockhawk.webservices.RetrofitStockRetrievalService;
import com.sam_chordas.android.stockhawk.webservices.StockRetrievalService;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class StockHistoryIntentService extends IntentService {
    private static final String LOG_TAG = StockHistoryIntentService.class.getSimpleName();

    public StockHistoryIntentService() {
        super("StockHistoryIntentService");
    }

    public StockHistoryIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "Starting Historical search intent...");
        if (intent != null) {
            final String symbol = intent.getStringExtra("symbol");
            final String startDate = intent.getStringExtra("startDate");
            final String endDate = intent.getStringExtra("endDate");

            StockRetrievalService stockRetrievalService = new RetrofitStockRetrievalService();
            ArrayList<Quote> history = (ArrayList<Quote>)stockRetrievalService.getHistoricalData(symbol,startDate,endDate);

            Log.d(LOG_TAG, "Retrieved history for " + symbol);
            Intent localIntent = new Intent(Constants.SYMBOL_HISTORY_SUCCESS)
                    .putParcelableArrayListExtra(Constants.SYMBOL_HISTORY_RESULTS, history)
                    .putExtra("symbol", symbol)
                    .putExtra("startDate", startDate)
                    .putExtra("endDate", endDate);


            // Broadcasts the Intent to receivers in this app.
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

        }
    }


}
