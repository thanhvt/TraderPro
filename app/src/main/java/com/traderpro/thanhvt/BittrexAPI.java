/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.traderpro.thanhvt;

/**
 *
 * @author ThanhVT
 */
import java.security.InvalidKeyException;

import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import java.net.*;
import java.io.*;

public class BittrexAPI {

    private static final String API_URL = "https://bittrex.com/api/v1.1/";

    static String apiKEY = null;
    static String apiSecretKEY = null;

    public static enum Bittrex {
        PUBLIC, ACCOUNT
    }

    public static String BittrexRequest(String Link, Bittrex options) {

        String inputLine = null;
        String bittrexData = null;

        switch (options) {
            case PUBLIC:
                try {
                    URLConnection conn = new URL(Link).openConnection();

                    while ((inputLine = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine()) != null) {
                        bittrexData = inputLine;
                    }

                    return bittrexData;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case ACCOUNT:
                try {
                    URLConnection conn = new URL(Link).openConnection();

                    conn.setConnectTimeout(5000);
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setRequestProperty("apisign", HmacSha512.ENCRYPT(Link, apiSecretKEY));

                    while ((inputLine = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine()) != null) {
                        bittrexData = inputLine;
                    }

                    return bittrexData;
                } catch (IOException | InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                break;
        }
        return "101";

    }

    public static long getUnixTime() {
        return System.currentTimeMillis() / 1000L;
    }

    public static void setAPIKeys(String apikey, String apisecretkey) {
        apiKEY = apikey;
        apiSecretKEY = apisecretkey;
    }

    // PUBLIC API
    public static String getCustom(String custom) {
        return BittrexRequest(API_URL + custom, Bittrex.PUBLIC);
    }

    public static String getMarkets() {
        return BittrexRequest(API_URL + "public/getmarkets", Bittrex.PUBLIC);
    }

    public static String getCurrencies() {
        return BittrexRequest(API_URL + "public/getcurrencies", Bittrex.PUBLIC);
    }

    public static String getMarketSummaries() {
        return BittrexRequest(API_URL + "public/getmarketsummaries", Bittrex.PUBLIC);
    }

    public static String getMarketSummary(String market) {
        return BittrexRequest(API_URL + "public/getmarketsummary?market=" + market, Bittrex.PUBLIC);
    }

    public static String getTicker(String market) {
        return BittrexRequest(API_URL + "public/getticker?market=" + market, Bittrex.PUBLIC);
    }

    public static String getOrderBook(String market) {
        return BittrexRequest(API_URL + "public/getorderbook?market=" + market, Bittrex.PUBLIC);
    }

    public static String getMarketHistory(String market) {
//	return BittrexRequest(API_URL + "public/getmarkethistory?market=" + market, Bittrex.PUBLIC);
        return BittrexRequest("https://bittrex.com/Api/v2.0/pub/market/GetTicks?marketName=BTC-WAVES&tickInterval=day", Bittrex.PUBLIC);

    }

    // ACCOUNT
    public static String getBalances() {
        return BittrexRequest(API_URL + "account/getbalances?apikey=" + apiKEY + "&nonce=" + getUnixTime(),
                Bittrex.ACCOUNT);
    }

    public static String getBalance(String Currency) {
        return BittrexRequest(API_URL + "account/getbalance?apikey=" + apiKEY + "&nonce=" + getUnixTime() + "&currency=" + Currency, Bittrex.ACCOUNT);
    }

    public static String getDepositAddress(String Currency) {
        return BittrexRequest(API_URL + "account/getdepositaddress?apikey=" + apiKEY + "&nonce=" + getUnixTime() + "&currency=" + Currency, Bittrex.ACCOUNT);
    }

    public static String getOrderHistory() {
        System.out.print(API_URL + "account/getorderhistory?apikey=" + apiKEY + "&nonce=" + getUnixTime());
        return BittrexRequest(API_URL + "account/getorderhistory?apikey=" + apiKEY + "&nonce=" + getUnixTime(), Bittrex.ACCOUNT);
    }

    public static String getOrder(String uuid) {
        return BittrexRequest(API_URL + "account/getorder?apikey=" + apiKEY + "&nonce=" + getUnixTime() + "&uuid=" + uuid, Bittrex.ACCOUNT);
    }

    public static String getWithdrawalHistory(String Currency) {
        return BittrexRequest(API_URL + "account/getwithdrawalhistory?apikey=" + apiKEY + "&nonce=" + getUnixTime() + "&currency=" + Currency, Bittrex.ACCOUNT);
    }

    public static String getDepositHistory(String Currency) {
        return BittrexRequest(API_URL + "account/getdeposithistory?apikey=" + apiKEY + "&nonce=" + getUnixTime() + "&currency=" + Currency, Bittrex.ACCOUNT);
    }

    public static String Withdraw(String Currency, String quantity, String address) {
        return BittrexRequest(API_URL + "account/withdraw?apikey=" + apiKEY + "&nonce=" + getUnixTime() + "&currency="
                + Currency + "&quantity=" + quantity + "&address=" + address, Bittrex.ACCOUNT);
    }

    // MARKET
    public static String buyLimit(String Currency, String quantity, String rate) {
        return BittrexRequest(API_URL + "market/buylimit?apikey=" + apiKEY + "&nonce=" + getUnixTime() + "&market=" + Currency
                + "&quantity=" + quantity + "&rate=" + rate, Bittrex.ACCOUNT);
    }

    public static String sellLimit(String Currency, String quantity, String rate) {
        return BittrexRequest(API_URL + "market/selllimit?apikey=" + apiKEY + "&nonce=" + getUnixTime() + "&market=" + Currency
                + "&quantity=" + quantity + "&rate=" + rate, Bittrex.ACCOUNT);
    }

    public static String cancelOrder(String uuid) {
        return BittrexRequest(API_URL + "market/cancel?apikey=" + apiKEY + "&nonce=" + getUnixTime() + "&uuid=" + uuid, Bittrex.ACCOUNT);
    }

    public static String getOpenOrders(String Currency) {
        return BittrexRequest(API_URL + "market/cancel?apikey=" + apiKEY + "&nonce=" + getUnixTime() + "&market=" + Currency, Bittrex.ACCOUNT);
    }

    public static String getMyOrders() {
        return BittrexRequest(API_URL + "market/getopenorders?apikey=" + apiKEY + "&nonce=" + getUnixTime(), Bittrex.ACCOUNT);
    }

    public static String getOrders() {
        return BittrexRequest(API_URL + "market/getOpenOrders?apikey=" + apiKEY + "&nonce=" + getUnixTime(), Bittrex.ACCOUNT);
    }

    public static String getMarketHistory(String market, String tick) {
        return BittrexRequest("https://bittrex.com/Api/v2.0/pub/market/GetTicks?marketName=" + market + "&tickInterval=" + tick, Bittrex.PUBLIC);
    }

    public static String layLichSuGia(String market){
        return BittrexRequest("https://bittrex.com/Api/v2.0/pub/market/GetTicks?marketName=" + market + "&tickInterval=hour", Bittrex.PUBLIC);
    }

    public static String getLatestTick(String market, String strTheoThoiGian){
        return BittrexRequest("https://bittrex.com/Api/v2.0/pub/market/GetLatestTick?marketName=" + market + "&tickInterval=" + strTheoThoiGian, Bittrex.PUBLIC);
    }
}
