package com.sam_chordas.android.stockhawk.webservices;

import java.util.List;

/**
 * StockHawk
 * Created by david on 2016-06-21.
 */
public interface StockRetrievalService {

    List<Quote> getHistoricalData(String symbol, String startDate, String endDate);
}
