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
}
