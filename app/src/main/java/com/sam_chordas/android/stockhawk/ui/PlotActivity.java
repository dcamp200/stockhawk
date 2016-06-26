package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.webservices.Quote;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class PlotActivity extends Activity {
    private XYPlot plot;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);
        TextView stockSymbolView = (TextView) findViewById(R.id.symbolName);
        TextView startDateView = (TextView) findViewById(R.id.startDate);
        TextView endDateView = (TextView) findViewById(R.id.endDate);
        TextView closingPriceView = (TextView) findViewById(R.id.closingPrice);
        TextView periodHighView = (TextView) findViewById(R.id.periodHigh);
        TextView periodLowView = (TextView) findViewById(R.id.periodLow);

        // Extract our data from the intent
        Intent intent = getIntent();
        ArrayList<Quote> quotes = intent.getParcelableArrayListExtra(Constants.SYMBOL_HISTORY_RESULTS);

        stockSymbolView.setText(intent.getStringExtra("symbol"));
        startDateView.setText(String.format(getResources().getString(R.string.from), intent.getStringExtra("startDate")));
        endDateView.setText(String.format(getResources().getString(R.string.to), intent.getStringExtra("endDate")));



        plot = (XYPlot) findViewById(R.id.linechart);

        List<Number> seriesNumbers = new ArrayList<>();

        Quote highQuote = null;
        Quote lowQuote = null;
        float closingPrice = 0;
        if (quotes.size()>0) {
            highQuote = quotes.get(0);
            lowQuote = quotes.get(0);
            closingPrice = Float.valueOf(quotes.get(quotes.size() - 1).getClose());

        }

        NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
        String closingPriceText = numberFormatter.format(closingPrice);
        closingPriceView.setText(String.format(getResources().getString(R.string.closing_price),closingPriceText));

        float high = 0;
        float periodHigh = 0;
        float low = 0;
        float periodLow = 0;

        for (Quote quote: quotes) {
            high = Float.valueOf(highQuote.getHigh());
            low  = Float.valueOf(lowQuote.getLow());
            if (high > periodHigh) {
                periodHigh = high;
                highQuote = quote;
            }

            if (low < periodLow) {
                periodLow = low;
                lowQuote = quote;
            }
            seriesNumbers.add(Float.valueOf(quote.getClose()));
        }

        String periodHighText = numberFormatter.format(high);
        String periodLowText = numberFormatter.format(low);
        periodHighView.setText(String.format(getResources().getString(R.string.high_price),periodHighText));
        periodLowView.setText(String.format(getResources().getString(R.string.low_price),periodLowText));

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        XYSeries series = new SimpleXYSeries(seriesNumbers, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Stock Price");

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:

        // Create a formatter to use for drawing a series using LineAndPointRenderer:
        LineAndPointFormatter seriesFormat = new LineAndPointFormatter(
                Color.rgb(0, 200, 0),                   // line color
                Color.rgb(0, 100, 0),                   // point color
                null,                                   // fill color (none)
                new PointLabelFormatter(Color.WHITE));

        plot.setDomainLabel("Period");
        plot.setRangeLabel("Stock Price");

        plot.getGraphWidget().setRangeTickLabelWidth(100);
        // reduce the number of range labels
        plot.setTicksPerRangeLabel(5);
        plot.setTicksPerDomainLabel(30);

        // add a new series' to the xyplot:
        plot.addSeries(series, seriesFormat);

    }
}
