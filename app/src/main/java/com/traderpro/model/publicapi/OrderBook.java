package com.traderpro.model.publicapi;

import com.traderpro.model.publicapi.OrderBookEntry;

import java.util.List;

/**
 * @author contact@elbatya.de
 */
public class OrderBook {

    private List<OrderBookEntry> buy;
    private List<OrderBookEntry> sell;

    public List<OrderBookEntry> getBuy() {
        return buy;
    }

    public void setBuy(List<OrderBookEntry> buy) {
        this.buy = buy;
    }

    public List<OrderBookEntry> getSell() {
        return sell;
    }

    public void setSell(List<OrderBookEntry> sell) {
        this.sell = sell;
    }
}
