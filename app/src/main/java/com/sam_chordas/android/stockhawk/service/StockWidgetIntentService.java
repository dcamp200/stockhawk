package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.ui.StockhawkAppWidgetProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class StockWidgetIntentService extends IntentService {
    private static final String LOG_TAG = StockWidgetIntentService.class.getSimpleName();

    public StockWidgetIntentService() {
        super("StockWidgetIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "Starting widget intent service...");
        if (intent != null) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, StockhawkAppWidgetProvider.class));

            List<Stock> stocks = queryStocks();

            for (int appWidget: appWidgetIds) {

//                RemoteViews views = new RemoteViews(getPackageName(), R.layout.stockhawk_app_widget_provider);
//                views.setRemoteAdapter(R.id.widget_list_view,);

            }

        }
    }


    private List<Stock> queryStocks() {
        Log.d(LOG_TAG, "Widget intent service: querying stocks..");
        List<Stock> stocks = new ArrayList<>();

        final String[] projection = {"Distinct " + QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE, QuoteColumns.ISUP, QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE};
        // Query the database for latest values
        Cursor cursor = getContentResolver().query(
                QuoteProvider.Quotes.CONTENT_URI,
                projection,
                null,
                null,
                null);

        if (null != cursor) {

            Stock stock = null;
            try {
                while (cursor.moveToNext()) {
                    stock = new Stock();
                    stock.symbol = cursor.getString(cursor.getColumnIndexOrThrow(QuoteColumns.SYMBOL));
                    stock.bidPrice = cursor.getString(cursor.getColumnIndexOrThrow(QuoteColumns.BIDPRICE));
                    stock.change = cursor.getString(cursor.getColumnIndexOrThrow(QuoteColumns.CHANGE));
                    stock.percentChange = cursor.getString(cursor.getColumnIndexOrThrow(QuoteColumns.PERCENT_CHANGE));
                    stock.isUp = cursor.getInt(cursor.getColumnIndexOrThrow(QuoteColumns.ISUP));
                    stocks.add(stock);
                }
            } finally {
                cursor.close();
            }
        }
        Log.d(LOG_TAG, "Found " + stocks.size() + " stocks");
        return stocks;
    }


    private class Stock {
        String symbol;
        String bidPrice;
        String change;
        int isUp;
        String percentChange;
    }



}
