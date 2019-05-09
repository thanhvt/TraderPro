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

    @Column(name = "strGia5P")
    public String strGia5P;

    @Column(name = "strGia4H")
    public String strGia4H;

    @Column(name = "strVol2H")
    public String strVol2H;
}
