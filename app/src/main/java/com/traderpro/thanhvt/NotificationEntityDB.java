package com.traderpro.thanhvt;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

@Table(name = "NotificationEntityDB")
public class NotificationEntityDB extends Model implements Serializable {

    @Column(name = "NGAY")
    public String NGAY;

    @Column(name = "strId")
    public String strId;

    @Column(name = "strExchange")
    public String strExchange;

    @Column(name = "strCoin")
    public String strCoin;

    @Column(name = "strGia")
    public String strGia;

    @Column(name = "strGiaBTC")
    public String strGiaBTC;

    @Column(name = "strVol")
    public String strVol;

    @Column(name = "strVolTB")
    public String strVolTB;

    @Column(name = "strTime")
    public String strTime;

    @Column(name = "strCase")
    public String strCase;

    @Column(name = "strGiaMax")
    public Double strGiaMax;

    @Column(name = "strTimeGiaMax")
    public String strTimeGiaMax;

    @Column(name = "strBuySell")
    public String strBuySell;

    @Column(name = "strTakerMaker")
    public String strTakerMaker;

    @Column(name = "strGiaMin")
    public Double strGiaMin;

    @Column(name = "strGiaBan")
    public String strGiaBan;

    @Column(name = "strTimeBan")
    public String strTimeBan;

    @Column(name = "strProfit")
    public String strProfit;

    @Column(name = "numberBuy")
    public String numberBuy = "0";

    @Column(name = "numberSell")
    public String numberSell = "0";

    @Column(name = "strGiaHienTai")
    public Double strGiaHienTai;

    @Column(name = "isSellNow")
    public boolean isSellNow = false;

    @Column(name = "strImageURL")
    public String strImageURL;

    @Column(name = "strGiaTrade")
    public String strGiaTrade;

    @Column(name = "tangSoLan")
    public Double tangSoLan;

    @Column(name = "strTimeFixProfit")
    public String strTimeFixProfit;

    @Column(name = "strGiaOP")
    public String strGiaOP;

    @Column(name = "strGia5P")
    public String strGia5P;

    @Column(name = "strGia30P")
    public String strGia30P;

    @Column(name = "strGia1H")
    public String strGia1H;

    @Column(name = "strGia2H")
    public String strGia2H;

    @Column(name = "strGia4H")
    public String strGia4H;

    @Column(name = "strVol1H")
    public String strVol1H;

    @Column(name = "strVol2H")
    public String strVol2H;

    //"BUYY" + "|" + strTradeID + "|" + newOrderResponse.getExecutedQty() + "|" + strGiaSan;
    @Column(name = "ACTION")
    public String ACTION;

    @Column(name = "strTradeID")
    public String strTradeID;

    @Column(name = "strExecutedQty")
    public String strExecutedQty;

    @Column(name = "strGiaSan")
    public String strGiaSan;

    @Override
    public String toString() {
        return "NotificationEntityDB{" +
                "NGAY='" + NGAY + '\'' +
                ", strId='" + strId + '\'' +
                ", strExchange='" + strExchange + '\'' +
                ", strCoin='" + strCoin + '\'' +
                ", strGia='" + strGia + '\'' +
                ", strGiaBTC='" + strGiaBTC + '\'' +
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
                ", strGiaOP='" + strGiaOP + '\'' +
                ", strGia5P='" + strGia5P + '\'' +
                ", strGia30P='" + strGia30P + '\'' +
                ", strGia1H='" + strGia1H + '\'' +
                ", strGia2H='" + strGia2H + '\'' +
                ", strGia4H='" + strGia4H + '\'' +
                ", strVol1H='" + strVol1H + '\'' +
                ", strVol2H='" + strVol2H + '\'' +
                ", ACTION='" + ACTION + '\'' +
                ", strTradeID='" + strTradeID + '\'' +
                ", strExecutedQty='" + strExecutedQty + '\'' +
                ", strGiaSan='" + strGiaSan + '\'' +
                '}';
    }
}
