package com.traderpro.thanhvt;

import java.io.Serializable;

public class NotificationEntity implements Serializable {
    public String strId;
    public String strExchange;
    public String strCoin;
    public String strGia;
    public String strVol;
    public String strVolTB;
    public String strTime;
    public String strCase;
    public Double strGiaMax;
    public String strTimeGiaMax;
    public String strBuySell;
    public String strTakerMaker;
    public Double strGiaMin;

    public String strGiaBan;
    public String strTimeBan;
    public String strProfit;
    public String numberBuy = "0";
    public String numberSell = "0";

    public Double strGiaHienTai;
    public boolean isSellNow = false;

    public String strImageURL;

    public String strGiaTrade;
    public Double tangSoLan;

    public String strTimeFixProfit;
//    public String strTradeID;
//    public String KLMuaBan;

    @Override
    public String toString() {
        return "NotificationEntity{" +
                "strId='" + strId + '\'' +
                ", strExchange='" + strExchange + '\'' +
                ", strCoin='" + strCoin + '\'' +
                ", strGia='" + strGia + '\'' +
                ", strVol='" + strVol + '\'' +
                ", strVolTB='" + strVolTB + '\'' +
                ", strTime='" + strTime + '\'' +
                ", strCase='" + strCase + '\'' +
                ", strGiaMax=" + strGiaMax +
                ", strTimeGiaMax='" + strTimeGiaMax + '\'' +
                ", strBuySell='" + strBuySell + '\'' +
                ", strTakerMaker='" + strTakerMaker + '\'' +
                ", strGiaMin=" + strGiaMin +
                ", strGiaBan='" + strGiaBan + '\'' +
                ", strTimeBan='" + strTimeBan + '\'' +
                ", strProfit='" + strProfit + '\'' +
                ", numberBuy='" + numberBuy + '\'' +
                ", numberSell='" + numberSell + '\'' +
                ", strGiaHienTai=" + strGiaHienTai +
                ", isSellNow=" + isSellNow +
                ", strImageURL='" + strImageURL + '\'' +
                ", strGiaTrade='" + strGiaTrade + '\'' +
                ", tangSoLan=" + tangSoLan +
                ", strTimeFixProfit='" + strTimeFixProfit + '\'' +
                '}';
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getStrExchange() {
        return strExchange;
    }

    public void setStrExchange(String strExchange) {
        this.strExchange = strExchange;
    }

    public String getStrCoin() {
        return strCoin;
    }

    public void setStrCoin(String strCoin) {
        this.strCoin = strCoin;
    }

    public String getStrGia() {
        return strGia;
    }

    public void setStrGia(String strGia) {
        this.strGia = strGia;
    }

    public String getStrVol() {
        return strVol;
    }

    public void setStrVol(String strVol) {
        this.strVol = strVol;
    }

    public String getStrVolTB() {
        return strVolTB;
    }

    public void setStrVolTB(String strVolTB) {
        this.strVolTB = strVolTB;
    }

    public String getStrTime() {
        return strTime;
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }

    public String getStrCase() {
        return strCase;
    }

    public void setStrCase(String strCase) {
        this.strCase = strCase;
    }

    public Double getStrGiaMax() {
        return strGiaMax;
    }

    public void setStrGiaMax(Double strGiaMax) {
        this.strGiaMax = strGiaMax;
    }

    public String getStrTimeGiaMax() {
        return strTimeGiaMax;
    }

    public void setStrTimeGiaMax(String strTimeGiaMax) {
        this.strTimeGiaMax = strTimeGiaMax;
    }

    public String getStrBuySell() {
        return strBuySell;
    }

    public void setStrBuySell(String strBuySell) {
        this.strBuySell = strBuySell;
    }

    public String getStrTakerMaker() {
        return strTakerMaker;
    }

    public void setStrTakerMaker(String strTakerMaker) {
        this.strTakerMaker = strTakerMaker;
    }

    public Double getStrGiaMin() {
        return strGiaMin;
    }

    public void setStrGiaMin(Double strGiaMin) {
        this.strGiaMin = strGiaMin;
    }

    public String getStrGiaBan() {
        return strGiaBan;
    }

    public void setStrGiaBan(String strGiaBan) {
        this.strGiaBan = strGiaBan;
    }

    public String getStrTimeBan() {
        return strTimeBan;
    }

    public void setStrTimeBan(String strTimeBan) {
        this.strTimeBan = strTimeBan;
    }

    public String getStrProfit() {
        return strProfit;
    }

    public void setStrProfit(String strProfit) {
        this.strProfit = strProfit;
    }

    public String getNumberBuy() {
        return numberBuy;
    }

    public void setNumberBuy(String numberBuy) {
        this.numberBuy = numberBuy;
    }

    public String getNumberSell() {
        return numberSell;
    }

    public void setNumberSell(String numberSell) {
        this.numberSell = numberSell;
    }

    public Double getStrGiaHienTai() {
        return strGiaHienTai;
    }

    public void setStrGiaHienTai(Double strGiaHienTai) {
        this.strGiaHienTai = strGiaHienTai;
    }

    public boolean isSellNow() {
        return isSellNow;
    }

    public void setSellNow(boolean sellNow) {
        isSellNow = sellNow;
    }

    public String getStrImageURL() {
        return strImageURL;
    }

    public void setStrImageURL(String strImageURL) {
        this.strImageURL = strImageURL;
    }

    public String getStrGiaTrade() {
        return strGiaTrade;
    }

    public void setStrGiaTrade(String strGiaTrade) {
        this.strGiaTrade = strGiaTrade;
    }

    public Double getTangSoLan() {
        return tangSoLan;
    }

    public void setTangSoLan(Double tangSoLan) {
        this.tangSoLan = tangSoLan;
    }

    public String getStrTimeFixProfit() {
        return strTimeFixProfit;
    }

    public void setStrTimeFixProfit(String strTimeFixProfit) {
        this.strTimeFixProfit = strTimeFixProfit;
    }
}
