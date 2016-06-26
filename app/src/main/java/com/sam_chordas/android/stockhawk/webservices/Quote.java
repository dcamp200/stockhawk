package com.sam_chordas.android.stockhawk.webservices;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * StockHawk
 * Created by david on 2016-06-25.
 */
public class Quote implements Parcelable {
    private String Symbol;
    private String Date;
    private String Open;
    private String High;
    private String Low;
    private String Close;
    private String Volume;
    private String Adj_Close;

    protected Quote(Parcel in) {
        Symbol = in.readString();
        Date = in.readString();
        Open = in.readString();
        High = in.readString();
        Low = in.readString();
        Close = in.readString();
        Volume = in.readString();
        Adj_Close = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Symbol);
        dest.writeString(Date);
        dest.writeString(Open);
        dest.writeString(High);
        dest.writeString(Low);
        dest.writeString(Close);
        dest.writeString(Volume);
        dest.writeString(Adj_Close);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getOpen() {
        return Open;
    }

    public void setOpen(String open) {
        Open = open;
    }

    public String getHigh() {
        return High;
    }

    public void setHigh(String high) {
        High = high;
    }

    public String getLow() {
        return Low;
    }

    public void setLow(String low) {
        Low = low;
    }

    public String getClose() {
        return Close;
    }

    public void setClose(String close) {
        Close = close;
    }

    public String getVolume() {
        return Volume;
    }

    public void setVolume(String volume) {
        Volume = volume;
    }

    public String getAdj_Close() {
        return Adj_Close;
    }

    public void setAdj_Close(String adj_Close) {
        Adj_Close = adj_Close;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "Symbol='" + Symbol + '\'' +
                ", Date='" + Date + '\'' +
                ", Open='" + Open + '\'' +
                ", High='" + High + '\'' +
                ", Low='" + Low + '\'' +
                ", Close='" + Close + '\'' +
                ", Volume='" + Volume + '\'' +
                ", Adj_Close='" + Adj_Close + '\'' +
                '}';
    }
}
