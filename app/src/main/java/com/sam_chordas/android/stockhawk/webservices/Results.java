package com.sam_chordas.android.stockhawk.webservices;

import java.util.List;

/**
 * StockHawk
 * Created by david on 2016-06-25.
 */
public class Results {
    private List<Quote> quote;

    public List<Quote> getQuote() {
        return quote;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quote = quotes;
    }

    @Override
    public String toString() {
        return "Results{" +
                "quote=" + quote +
                '}';
    }
}
