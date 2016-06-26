package com.sam_chordas.android.stockhawk.webservices;

/**
 * StockHawk
 * Created by david on 2016-06-25.
 */
public class QueryResponse {
    private Query query;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "QueryResponse{" +
                "query=" + query +
                '}';
    }
}
