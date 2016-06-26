package com.sam_chordas.android.stockhawk.webservices;

import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * StockHawk
 * Created by david on 2016-06-22.
 */
public class RetrofitStockRetrievalService implements StockRetrievalService {
    private static final String LOG_TAG = RetrofitStockRetrievalService.class.getSimpleName();
    private static final String ENDPOINT = "https://query.yahooapis.com";
    private static final String QUERY ="select * from yahoo.finance.historicaldata where symbol = \"%s\" and startDate = \"%s\" and endDate = \"%s\"";

    private Retrofit retrofit;
    private YahooFinanceService service;


    public RetrofitStockRetrievalService() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(YahooFinanceService.class);
    }


    @Override
    public List<Quote> getHistoricalData(String symbol, String startDate, String endDate) {
        String query = String.format(QUERY, symbol, startDate, endDate);
        Log.d(LOG_TAG, "***** QUERY :" + query);
        Call<QueryResponse> call = service.queryHistoricalData(query);
        try {
            Response<QueryResponse>response = call.execute();
            if (response.isSuccessful()) {
                // We need the order from oldest to newest
                List<Quote> results = response.body().getQuery().getResults().getQuote();
                Collections.reverse(results);
                return results;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }


    public interface YahooFinanceService {
        @GET("/v1/public/yql?format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=")
        Call<QueryResponse> queryHistoricalData(@retrofit2.http.Query("q") String yql);
    }


}
