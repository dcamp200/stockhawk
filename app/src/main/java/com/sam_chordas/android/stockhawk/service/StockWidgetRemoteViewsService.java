package com.sam_chordas.android.stockhawk.service;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * StockHawk
 * Created by david on 2016-07-01.
 */
public class StockWidgetRemoteViewsService extends RemoteViewsService {
    private final String LOG_TAG = StockWidgetRemoteViewsService.class.getSimpleName();


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(LOG_TAG, "Stock Widget onGetViewFactory..");
        return new StockWidgetRemoteViewsFactory(this.getApplicationContext());
    }



    class StockWidgetRemoteViewsFactory implements RemoteViewsFactory {
        private final String LOG_TAG = StockWidgetRemoteViewsFactory.class.getSimpleName();
        private List<Stock> mStocks = new ArrayList<>();
        private Context mContext;


        public StockWidgetRemoteViewsFactory(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public void onCreate() {
            Log.d(LOG_TAG, "Stock Widget RemoteFactory OnCreate..");
            mStocks = queryStocks();
        }

        @Override
        public void onDataSetChanged() {
            Log.d(LOG_TAG, "OnDatasetchanged...");
            // This method is called by the app hosting the widget (e.g., the launcher)
            // However, our ContentProvider is not exported so it doesn't have access to the
            // data. Therefore we need to clear (and finally restore) the calling identity so
            // that calls use our process and permission
            final long identityToken = Binder.clearCallingIdentity();
            mStocks = queryStocks();
            Binder.restoreCallingIdentity(identityToken);
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            Log.d( LOG_TAG, "Returning count  " + mStocks.size());
            return mStocks.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Log.d(LOG_TAG, "Getting view for stock " + position);
            if (position == AdapterView.INVALID_POSITION) {
                return null;
            }

            Stock stock = mStocks.get(position);
            Log.d(LOG_TAG, "Building remote view for stock :" + stock);
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.list_item_quote_widget);
            remoteViews.setTextViewText(R.id.stock_symbol, stock.symbol);
            remoteViews.setTextViewText(R.id.bid_price, stock.bidPrice);
            remoteViews.setTextViewText(R.id.change, stock.change);

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return new RemoteViews(getPackageName(), R.layout.list_item_quote);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        private List<Stock> queryStocks() {
            Log.d(LOG_TAG, "Widget remote views factory: querying stocks..");
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
                        Log.d(LOG_TAG, "Adding stock:" + stock);
                        stocks.add(stock);
                    }
                } finally {
                    Log.d(LOG_TAG, "Closing cursor");
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

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public String getBidPrice() {
                return bidPrice;
            }

            public void setBidPrice(String bidPrice) {
                this.bidPrice = bidPrice;
            }

            public String getChange() {
                return change;
            }

            public void setChange(String change) {
                this.change = change;
            }

            public int getIsUp() {
                return isUp;
            }

            public void setIsUp(int isUp) {
                this.isUp = isUp;
            }

            public String getPercentChange() {
                return percentChange;
            }

            public void setPercentChange(String percentChange) {
                this.percentChange = percentChange;
            }

            @Override
            public String toString() {
                return "Stock{" +
                        "symbol='" + symbol + '\'' +
                        ", bidPrice='" + bidPrice + '\'' +
                        ", change='" + change + '\'' +
                        ", isUp=" + isUp +
                        ", percentChange='" + percentChange + '\'' +
                        '}';
            }
        }


    }
}
