package com.sam_chordas.android.stockhawk.ui;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.service.StockWidgetRemoteViewsService;

/**
 * Implementation of App Widget functionality.
 */
public class StockhawkAppWidgetProvider extends AppWidgetProvider {
    private static final String LOG_TAG = StockhawkAppWidgetProvider.class.getSimpleName();



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(LOG_TAG, "Widget provider onUpdate..");

        for(int appWidgetId: appWidgetIds) {
            Log.d(LOG_TAG, "Updating widget: " + appWidgetId);

            // Instantiate the RemoteViews object for the app widget layout.
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stockhawk_app_widget_provider);

            // Set up the intent that starts the StackViewService, which will
            // provide the views for this collection.
            Intent intent = new Intent(context, StockWidgetRemoteViewsService.class);
            //intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            //intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // Set up the RemoteViews object to use a RemoteViews adapter.
            // This adapter connects
            // to a RemoteViewsService  through the specified intent.
            // This is how you populate the data.
            views.setRemoteAdapter(R.id.widget_list_view, intent);

            // The empty view is displayed when the collection has no items.
            // It should be in the same layout used to instantiate the RemoteViews
            // object above.
            views.setEmptyView(R.id.widget_list_view, R.id.widget_stockview_empty);

//            Intent onClickIntent = new Intent(context, MyStocksActivity.class);
//            PendingIntent onClickPendingIntent = PendingIntent.getActivity(context,0,onClickIntent,0);
//            views.setOnClickPendingIntent(R.id.widget_list_view,onClickPendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }


        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        Log.d(LOG_TAG, "Widget provider onReceive.. Action:" + action);

        if (Constants.SYMBOL_LOOKUP_SUCCESS.equals(intent.getAction())) {
            Log.d(LOG_TAG, "Updating stocks...");

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
        }







    }
}

