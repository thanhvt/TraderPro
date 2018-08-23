package com.traderpro.thanhvt;

import java.io.Serializable;

public class BotSignalEntity implements Serializable{
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
}
