package com.traderpro.thanhvt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {

    TextView tvThangThua, tvTT1, tvTT2, tvTT3, tvTT4, tvTT5, tvTT6, tvTT7, tvTT8, tvTT9, tvTT10, tvTT11, tvTT12, tvTT13;
    TextView tvTyLe1, tvTyLe2, tvTyLe3, tvTyLe4, tvTyLe5, tvTyLe6;
    TextView tvThangNua;

//    TextView tvThua1, tvThua2, tvThua3;

    TextView tvTitle1, tvTitle2, tvTitle3, tvTitle4, tvTitle5, tvTitle6, tvTitle7, tvTitle8, tvTitle9, tvTitle10, tvTitle11, tvTitle12, tvTitle13;
    TextView tvTitleThang1, tvTitleThang2;
    TextView tvTitleAn1, tvTitleAn2, tvTitleAn3, tvTitleAn4, tvTitleAn5, tvTitleAn6;
    TextView tvTitleThua1, tvTitleThua2, tvTitleThua3, tvTitleThua4;

//    Executor threadPoolExecutor;
//    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
//    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
//    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
//    private static final BlockingQueue<Runnable> sPoolWorkQueue =
//            new LinkedBlockingQueue<Runnable>(128);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        try {

            int corePoolSize = 80;
            int maximumPoolSize = 100;
            int keepAliveTime = 10;

//            BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
//            threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, sPoolWorkQueue);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

            initUI();

            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();

            ArrayList<NotificationEntity> lstEntity =
                    (ArrayList<NotificationEntity>) bundle.getSerializable("DATA");
            String strTime = bundle.getString("TIME");
            String strTinhToan = bundle.getString("TINHTOAN");
            int ngay = bundle.getInt("NGAY");
            int thang = bundle.getInt("THANG");
            int nam = bundle.getInt("NAM");

            ((AppCompatActivity) this).getSupportActionBar().setTitle("Trading Statistics " + strTime);

            int countThangThua = 0, countThangNua = 0;
            int tong1 = 0, thang1 = 0, tong2 = 0, thang2 = 0, tong3 = 0, thang3 = 0, tong4 = 0, thang4 = 0, tong5 = 0, thang5 = 0,
                    tong6 = 0, thang6 = 0, tong7 = 0, thang7 = 0, tong8 = 0, thang8 = 0, tong9 = 0, thang9 = 0,
                    tong10 = 0, thang10 = 0, tong11 = 0, thang11 = 0, tong12 = 0, thang12 = 0, tong13 = 0, thang13 = 0;
            String strThang1 = "", strThang2 = "", strThang3 = "", strThang4 = "", strThang5 = "", strThang6 = "", strThang7 = "",
                    strThang8 = "", strThang9 = "", strThang10 = "", strThang11 = "", strThang12 = "", strThang13 = "";
            int an1 = 0, an2 = 0, an3 = 0, an4 = 0, an5 = 0, an6 = 0;
            int case1_vol1 = 0, case1_vol2 = 0, case1_vol3 = 0, case1_vol4 = 0, case1_vol5 = 0, case1_vol6 = 0, case1_vol10 = 0;
            int case2_vol1 = 0, case2_vol2 = 0, case2_vol3 = 0, case2_vol4 = 0, case2_vol5 = 0, case2_vol6 = 0, case2_vol10 = 0;
            int case3_vol1 = 0, case3_vol2 = 0, case3_vol3 = 0, case3_vol4 = 0, case3_vol5 = 0, case3_vol6 = 0, case3_vol10 = 0;
            int case4_vol1 = 0, case4_vol2 = 0, case4_vol3 = 0, case4_vol4 = 0, case4_vol5 = 0, case4_vol6 = 0, case4_vol10 = 0;
            int case5_vol1 = 0, case5_vol2 = 0, case5_vol3 = 0, case5_vol4 = 0, case5_vol5 = 0, case5_vol6 = 0, case5_vol10 = 0;
            int case6_vol1 = 0, case6_vol2 = 0, case6_vol3 = 0, case6_vol4 = 0, case6_vol5 = 0, case6_vol6 = 0, case6_vol10 = 0;

//        int countThua1 = 0, countThua2 = 0, countThua3 = 0;
            double maxt1 = 100, maxd1 = 0, maxt2 = 100, maxd2 = 0, maxt3 = 100, maxd3 = 0, maxt4 = 100, maxd4 = 0, maxt5 = 100, maxd5 = 0, maxt6 = 100, maxd6 = 0,
                    maxt7 = 100, maxd7 = 0, maxt8 = 100, maxd8 = 0, maxt9 = 100, maxd9 = 0, maxt10 = 100, maxd10 = 0, maxt11 = 100, maxd11 = 0, maxt12 = 100, maxd12 = 0, maxt13 = 100, maxd13 = 0;

            for (NotificationEntity en : lstEntity) {
                Double giaBao = Double.parseDouble(en.strGia);
                Double giaMax = en.strGiaMax;
//            Double giaMin = en.strGiaMin;
                Double volHT = Double.parseDouble(en.strVol);
                Double volTB = Double.parseDouble(en.strVolTB);
                Double dProfit = ((en.strGiaMax - giaBao) / giaBao) * 100;
                //
//            if (giaMin != null) {
//                if (giaMin < giaBao * 0.995) {
//                    countThua1++;
//                }
//                if (giaMin < giaBao * 0.99) {
//                    countThua2++;
//                }
//                if (giaMin < giaBao * 0.97) {
//                    countThua3++;
//                }
//            }
                Log.e("Rep", en.toString());
//                String[] mTime = en.strTime.split(":");
//                Calendar c = Calendar.getInstance();
//                c.set(nam, thang - 1, ngay, Integer.parseInt(mTime[0]), Integer.parseInt(mTime[1]));
//                ExchangeGetPrice ex = new ExchangeGetPrice(en);
//                Object res = ex.executeOnExecutor(threadPoolExecutor, en.strCoin, c.getTimeInMillis() + "").get();


                if (giaMax > giaBao * 1.005 && en.strGiaMin > 0.95 * giaBao) {
                    countThangNua++;
                }
                if (giaMax > giaBao * 1.01 && en.strGiaMin > 0.95 * giaBao) {
                    countThangThua++;

                    if (en.strCase.equals("1")) {
                        thang1++;

                        if (dProfit < maxt1) maxt1 = dProfit;
                        if (dProfit > maxd1) {
                            maxd1 = dProfit;
                        }
                        strThang1 += String.format("%.1f", dProfit) + "---";
                    }
                    if (en.strCase.equals("2")) {
                        thang2++;

                        if (dProfit < maxt2) maxt2 = dProfit;
                        if (dProfit > maxd2) {
                            maxd2 = dProfit;
                        }
                        strThang2 += String.format("%.1f", dProfit) + "---";
                    }
                    if (en.strCase.equals("3")) {
                        thang3++;

                        if (dProfit < maxt3) maxt3 = dProfit;
                        if (dProfit > maxd3) maxd3 = dProfit;
                        strThang3 += String.format("%.1f", dProfit) + "---";
                    }
                    if (en.strCase.equals("4")) {
                        thang4++;

                        if (dProfit < maxt4) maxt4 = dProfit;
                        if (dProfit > maxd4) {
                            maxd4 = dProfit;
                        }
                        strThang4 += String.format("%.1f", dProfit) + "---";
                    }
                    if (en.strCase.equals("5")) {
                        thang5++;

                        if (dProfit < maxt5) maxt5 = dProfit;
                        if (dProfit > maxd5) {
                            maxd5 = dProfit;
                        }
                        strThang5 += String.format("%.1f", dProfit) + "---";
                    }
                    if (en.strCase.equals("6")) {
                        thang6++;

                        if (dProfit < maxt6) maxt6 = dProfit;
                        if (dProfit > maxd6) {
                            maxd6 = dProfit;
                        }
                        strThang6 += String.format("%.1f", dProfit) + "---";
                    }
                    if (en.strCase.equals("7")) {
                        thang7++;

                        if (dProfit < maxt7) maxt7 = dProfit;
                        if (dProfit > maxd7) {
                            maxd7 = dProfit;
                        }
                        strThang7 += String.format("%.1f", dProfit) + "---";
                    }
                    if (en.strCase.equals("8")) {
                        thang8++;

                        if (dProfit < maxt8) maxt8 = dProfit;
                        if (dProfit > maxd8) {
                            maxd8 = dProfit;
                        }
                        strThang8 += String.format("%.1f", dProfit) + "---";
                    }
                    if (en.strCase.equals("9")) {
                        thang9++;

                        if (dProfit < maxt9) maxt9 = dProfit;
                        if (dProfit > maxd9) {
                            maxd9 = dProfit;
                        }
                        strThang9 += String.format("%.1f", dProfit) + "---";
                    }
                    if (en.strCase.equals("10")) {
                        thang10++;

                        if (dProfit < maxt10) maxt10 = dProfit;
                        if (dProfit > maxd10) {
                            maxd10 = dProfit;
                        }
                        strThang10 += String.format("%.1f", dProfit) + "---";
                    }
                    if (en.strCase.equals("11")) {
                        thang11++;

                        if (dProfit < maxt11) maxt11 = dProfit;
                        if (dProfit > maxd11) {
                            maxd11 = dProfit;
                        }
                        strThang11 += String.format("%.1f", dProfit) + "---";
                    }
                    if (en.strCase.equals("12")) {
                        thang12++;

                        if (dProfit < maxt12) maxt12 = dProfit;
                        if (dProfit > maxd12) {
                            maxd12 = dProfit;
                        }
                        strThang12 += String.format("%.1f", dProfit) + "---";
                    }
                    if (en.strCase.equals("13")) {
                        thang13++;

                        if (dProfit < maxt13) maxt13 = dProfit;
                        if (dProfit > maxd13) {
                            maxd13 = dProfit;
                        }
                        strThang13 += String.format("%.1f", dProfit) + "---";
                    }
                }
                if (en.strCase.equals("1") && en.strGiaMax > 0) {
                    tong1++;
                }
                if (en.strCase.equals("2") && en.strGiaMax > 0) {
                    tong2++;
                }
                if (en.strCase.equals("3") && en.strGiaMax > 0) {
                    tong3++;
                }
                if (en.strCase.equals("4") && en.strGiaMax > 0) {
                    tong4++;
                }
                if (en.strCase.equals("5") && en.strGiaMax > 0) {
                    tong5++;
                }
                if (en.strCase.equals("6") && en.strGiaMax > 0) {
                    tong6++;
                }
                if (en.strCase.equals("7") && en.strGiaMax > 0) {
                    tong7++;
                }
                if (en.strCase.equals("8") && en.strGiaMax > 0) {
                    tong8++;
                }
                if (en.strCase.equals("9") && en.strGiaMax > 0) {
                    tong9++;
                }
                if (en.strCase.equals("10") && en.strGiaMax > 0) {
                    tong10++;
                }
                if (en.strCase.equals("11") && en.strGiaMax > 0) {
                    tong11++;
                }
                if (en.strCase.equals("12") && en.strGiaMax > 0) {
                    tong12++;
                }
                if (en.strCase.equals("13") && en.strGiaMax > 0) {
                    tong13++;
                }
//
                if (giaMax >= giaBao * 1.01 && giaMax < giaBao * 1.02) {
                    an1++;
                    if (volHT < volTB * 2) {
                        case1_vol1++;
                    } else if (volTB * 2 <= volHT && volHT < volTB * 3) {
                        case1_vol2++;
                    } else if (volTB * 3 <= volHT && volHT < volTB * 4) {
                        case1_vol3++;
                    } else if (volTB * 4 <= volHT && volHT < volTB * 5) {
                        case1_vol4++;
                    } else if (volTB * 5 <= volHT && volHT < volTB * 6) {
                        case1_vol5++;
                    } else if (volTB * 6 <= volHT && volHT < volTB * 10) {
                        case1_vol6++;
                    } else if (volTB * 10 <= volHT) {
                        case1_vol10++;
                    }
                }
                if (giaMax >= giaBao * 1.02 && giaMax < giaBao * 1.03) {
                    an2++;
                    if (volHT < volTB * 2) {
                        case2_vol1++;
                    } else if (volTB * 2 <= volHT && volHT < volTB * 3) {
                        case2_vol2++;
                    } else if (volTB * 3 <= volHT && volHT < volTB * 4) {
                        case2_vol3++;
                    } else if (volTB * 4 <= volHT && volHT < volTB * 5) {
                        case2_vol4++;
                    } else if (volTB * 5 <= volHT && volHT < volTB * 6) {
                        case2_vol5++;
                    } else if (volTB * 6 <= volHT && volHT < volTB * 10) {
                        case2_vol6++;
                    } else if (volTB * 10 <= volHT) {
                        case2_vol10++;
                    }
                }
                if (giaMax >= giaBao * 1.03 && giaMax < giaBao * 1.04) {
                    an3++;
                    if (volHT < volTB * 2) {
                        case3_vol1++;
                    } else if (volTB * 2 <= volHT && volHT < volTB * 3) {
                        case3_vol2++;
                    } else if (volTB * 3 <= volHT && volHT < volTB * 4) {
                        case3_vol3++;
                    } else if (volTB * 4 <= volHT && volHT < volTB * 5) {
                        case3_vol4++;
                    } else if (volTB * 5 <= volHT && volHT < volTB * 6) {
                        case3_vol5++;
                    } else if (volTB * 6 <= volHT && volHT < volTB * 10) {
                        case3_vol6++;
                    } else if (volTB * 10 <= volHT) {
                        case3_vol10++;
                    }
                }
                if (giaMax >= giaBao * 1.04 && giaMax < giaBao * 1.05) {
                    an4++;
                    if (volHT < volTB * 2) {
                        case4_vol1++;
                    } else if (volTB * 2 <= volHT && volHT < volTB * 3) {
                        case4_vol2++;
                    } else if (volTB * 3 <= volHT && volHT < volTB * 4) {
                        case4_vol3++;
                    } else if (volTB * 4 <= volHT && volHT < volTB * 5) {
                        case4_vol4++;
                    } else if (volTB * 5 <= volHT && volHT < volTB * 6) {
                        case4_vol5++;
                    } else if (volTB * 6 <= volHT && volHT < volTB * 10) {
                        case4_vol6++;
                    } else if (volTB * 10 <= volHT) {
                        case4_vol10++;
                    }
                }
                if (giaMax >= giaBao * 1.05 && giaMax < giaBao * 1.06) {
                    an5++;
                    if (volHT < volTB * 2) {
                        case5_vol1++;
                    } else if (volTB * 2 <= volHT && volHT < volTB * 3) {
                        case5_vol2++;
                    } else if (volTB * 3 <= volHT && volHT < volTB * 4) {
                        case5_vol3++;
                    } else if (volTB * 4 <= volHT && volHT < volTB * 5) {
                        case5_vol4++;
                    } else if (volTB * 5 <= volHT && volHT < volTB * 6) {
                        case5_vol5++;
                    } else if (volTB * 6 <= volHT && volHT < volTB * 10) {
                        case5_vol6++;
                    } else if (volTB * 10 <= volHT) {
                        case5_vol10++;
                    }
                }
                if (giaMax >= giaBao * 1.06) {
                    an6++;
                    if (volHT < volTB * 2) {
                        case6_vol1++;
                    } else if (volTB * 2 <= volHT && volHT < volTB * 3) {
                        case6_vol2++;
                    } else if (volTB * 3 <= volHT && volHT < volTB * 4) {
                        case6_vol3++;
                    } else if (volTB * 4 <= volHT && volHT < volTB * 5) {
                        case6_vol4++;
                    } else if (volTB * 5 <= volHT && volHT < volTB * 6) {
                        case6_vol5++;
                    } else if (volTB * 6 <= volHT && volHT < volTB * 10) {
                        case6_vol6++;
                    } else if (volTB * 10 <= volHT) {
                        case6_vol10++;
                    }
                }
            }

//            SharedPreferences pref = getApplication().getSharedPreferences(Config.NGON_NGU, 0);
//            String strNN = pref.getString("NN", "VN");
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            String strNN = sharedPrefs.getBoolean("NN", true) == true ? "VN" : "EN";
//        countThua1 = countThua1 / 4;
//        countThua2 = countThua2 / 4;
//        countThua3 = countThua3 / 4;
            if (strNN.equalsIgnoreCase("VN")) {
//            tvThua1.setText(countThua1 + " lần");
//            tvThua2.setText(countThua2 + " lần");
//            tvThua3.setText(countThua3 + " lần");

                tvThangThua.setText(countThangThua + "/" + strTinhToan + " lần");
                tvThangNua.setText(countThangNua + "/" + strTinhToan + " lần");
                if (thang1 > 5)
                    tvTT1.setText(thang1 + "/" + tong1 + " lần: " + String.format("%.1f", maxt1 == 100 ? 0 : maxt1) + "% >>> " + String.format("%.1f", maxd1) + "%");
                else
                    tvTT1.setText(thang1 + "/" + tong1 + " lần: " + strThang1);
                if (thang2 > 5)
                    tvTT2.setText(thang2 + "/" + tong2 + " lần: " + String.format("%.1f", maxt2 == 100 ? 0 : maxt2) + "% >>> " + String.format("%.1f", maxd2) + "%");
                else
                    tvTT2.setText(thang2 + "/" + tong2 + " lần: " + strThang2);
                if (thang3 > 5)
                    tvTT3.setText(thang3 + "/" + tong3 + " lần: " + String.format("%.1f", maxt3 == 100 ? 0 : maxt3) + "% >>> " + String.format("%.1f", maxd3) + "%");
                else
                    tvTT3.setText(thang3 + "/" + tong3 + " lần: " + strThang3);
                if (thang4 > 5)
                    tvTT4.setText(thang4 + "/" + tong4 + " lần: " + String.format("%.1f", maxt4 == 100 ? 0 : maxt4) + "% >>> " + String.format("%.1f", maxd4) + "%");
                else
                    tvTT4.setText(thang4 + "/" + tong4 + " lần: " + strThang4);
                if (thang5 > 5)
                    tvTT5.setText(thang5 + "/" + tong5 + " lần: " + String.format("%.1f", maxt5 == 100 ? 0 : maxt5) + "% >>> " + String.format("%.1f", maxd5) + "%");
                else
                    tvTT5.setText(thang5 + "/" + tong5 + " lần: " + strThang5);
                if (thang6 > 5)
                    tvTT6.setText(thang6 + "/" + tong6 + " lần: " + String.format("%.1f", maxt6 == 100 ? 0 : maxt6) + "% >>> " + String.format("%.1f", maxd6) + "%");
                else
                    tvTT6.setText(thang6 + "/" + tong6 + " lần: " + strThang6);
                if (thang7 > 5)
                    tvTT7.setText(thang7 + "/" + tong7 + " lần: " + String.format("%.1f", maxt7 == 100 ? 0 : maxt7) + "% >>> " + String.format("%.1f", maxd7) + "%");
                else
                    tvTT7.setText(thang7 + "/" + tong7 + " lần: " + strThang7);
                if (thang8 > 5)
                    tvTT8.setText(thang8 + "/" + tong8 + " lần: " + String.format("%.1f", maxt8 == 100 ? 0 : maxt8) + "% >>> " + String.format("%.1f", maxd8) + "%");
                else
                    tvTT8.setText(thang8 + "/" + tong8 + " lần: " + strThang8);
                if (thang9 > 5)
                    tvTT9.setText(thang9 + "/" + tong9 + " lần: " + String.format("%.1f", maxt9 == 100 ? 0 : maxt9) + "% >>> " + String.format("%.1f", maxd9) + "%");
                else
                    tvTT9.setText(thang9 + "/" + tong9 + " lần: " + strThang9);
                if (thang10 > 5)
                    tvTT10.setText(thang10 + "/" + tong10 + " lần: " + String.format("%.1f", maxt10 == 100 ? 0 : maxt10) + "% >>> " + String.format("%.1f", maxd10) + "%");
                else
                    tvTT10.setText(thang10 + "/" + tong10 + " lần: " + strThang10);
                if (thang11 > 5)
                    tvTT11.setText(thang11 + "/" + tong11 + " lần: " + String.format("%.1f", maxt11 == 100 ? 0 : maxt11) + "% >>> " + String.format("%.1f", maxd11) + "%");
                else
                    tvTT11.setText(thang11 + "/" + tong11 + " lần: " + strThang11);
                if (thang12 > 5)
                    tvTT12.setText(thang12 + "/" + tong12 + " lần: " + String.format("%.1f", maxt12 == 100 ? 0 : maxt12) + "% >>> " + String.format("%.1f", maxd12) + "%");
                else
                    tvTT12.setText(thang12 + "/" + tong12 + " lần: " + strThang12);
                if (thang13 > 5)
                    tvTT13.setText(thang13 + "/" + tong13 + " lần: " + String.format("%.1f", maxt13 == 100 ? 0 : maxt13) + "% >>> " + String.format("%.1f", maxd13) + "%");
                else
                    tvTT13.setText(thang13 + "/" + tong13 + " lần: " + strThang13);

                tvTitleThang1.setText("Tỷ lệ thắng > 0.5%");
                tvTitleThang2.setText("Tỷ lệ thắng > 1%");
                tvTitle1.setText("Thuật toán 1 thắng");
                tvTitle2.setText("Thuật toán 2 thắng");
                tvTitle3.setText("Thuật toán 3 thắng");
                tvTitle4.setText("Thuật toán 4 thắng");
                tvTitle5.setText("Thuật toán 5 thắng");
                tvTitle6.setText("Thuật toán 6 thắng");
                tvTitle7.setText("Thuật toán 7 thắng");
                tvTitle8.setText("Thuật toán 8 thắng");
                tvTitle9.setText("Thuật toán 9 thắng");
                tvTitle10.setText("Thuật toán 10 thắng");
                tvTitle11.setText("Thuật toán 11 thắng");
                tvTitle12.setText("Thuật toán 12 thắng");
                tvTitle13.setText("Thuật toán 13 thắng");

                tvTitleAn1.setText("Tỷ lệ ăn ~ 1%");
                tvTitleAn2.setText("Tỷ lệ ăn ~ 2%");
                tvTitleAn3.setText("Tỷ lệ ăn ~ 3%");
                tvTitleAn4.setText("Tỷ lệ ăn ~ 4%");
                tvTitleAn5.setText("Tỷ lệ ăn ~ 5%");
                tvTitleAn6.setText("Tỷ lệ ăn > 6%");

                tvTitleThua1.setText("Tỷ lệ thua ~ 0.5%");
                tvTitleThua2.setText("Tỷ lệ thua ~ 1%");
                tvTitleThua3.setText("Tỷ lệ thua > 3%");
                tvTitleThua4.setText("Tỷ lệ thua > 5%");
            } else {
//            tvThua1.setText(countThua1 + " times");
//            tvThua2.setText(countThua2 + " times");
//            tvThua3.setText(countThua3 + " times");

                tvThangThua.setText(countThangThua + "/" + strTinhToan + " note");
                tvThangNua.setText(countThangNua + "/" + strTinhToan + " note");
                tvTT1.setText(thang1 + "/" + tong1 + " note: " + String.format("%.1f", maxt1 == 100 ? 0 : maxt1) + "% >>> " + String.format("%.1f", maxd1) + "%");
                tvTT2.setText(thang2 + "/" + tong2 + " note: " + String.format("%.1f", maxt2 == 100 ? 0 : maxt2) + "% >>> " + String.format("%.1f", maxd2) + "%");
                tvTT3.setText(thang3 + "/" + tong3 + " note: " + String.format("%.1f", maxt3 == 100 ? 0 : maxt3) + "% >>> " + String.format("%.1f", maxd3) + "%");
                tvTT4.setText(thang4 + "/" + tong4 + " note: " + String.format("%.1f", maxt4 == 100 ? 0 : maxt4) + "% >>> " + String.format("%.1f", maxd4) + "%");
                tvTT5.setText(thang5 + "/" + tong5 + " note: " + String.format("%.1f", maxt5 == 100 ? 0 : maxt5) + "% >>> " + String.format("%.1f", maxd5) + "%");
                tvTT6.setText(thang6 + "/" + tong6 + " note: " + String.format("%.1f", maxt6 == 100 ? 0 : maxt6) + "% >>> " + String.format("%.1f", maxd6) + "%");
                tvTT7.setText(thang7 + "/" + tong7 + " note: " + String.format("%.1f", maxt7 == 100 ? 0 : maxt7) + "% >>> " + String.format("%.1f", maxd7) + "%");
                tvTT8.setText(thang8 + "/" + tong8 + " note: " + String.format("%.1f", maxt8 == 100 ? 0 : maxt8) + "% >>> " + String.format("%.1f", maxd8) + "%");
                tvTT9.setText(thang9 + "/" + tong9 + " note: " + String.format("%.1f", maxt9 == 100 ? 0 : maxt9) + "% >>> " + String.format("%.1f", maxd9) + "%");
                tvTT10.setText(thang10 + "/" + tong10 + " note: " + String.format("%.1f", maxt10 == 100 ? 0 : maxt10) + "% >>> " + String.format("%.1f", maxd10) + "%");
                tvTT11.setText(thang11 + "/" + tong11 + " note: " + String.format("%.1f", maxt11 == 100 ? 0 : maxt11) + "% >>> " + String.format("%.1f", maxd11) + "%");
                tvTT12.setText(thang12 + "/" + tong12 + " note: " + String.format("%.1f", maxt12 == 100 ? 0 : maxt12) + "% >>> " + String.format("%.1f", maxd12) + "%");
                tvTT13.setText(thang13 + "/" + tong13 + " note: " + String.format("%.1f", maxt13 == 100 ? 0 : maxt13) + "% >>> " + String.format("%.1f", maxd13) + "%");

                tvTitleThang1.setText("Rate win > 0.5%");
                tvTitleThang2.setText("Rate win > 1%");
                tvTitle1.setText("Algorithm 1 Success");
                tvTitle2.setText("Algorithm 2 Success");
                tvTitle3.setText("Algorithm 3 Success");
                tvTitle4.setText("Algorithm 4 Success");
                tvTitle5.setText("Algorithm 5 Success");
                tvTitle6.setText("Algorithm 6 Success");
                tvTitle7.setText("Algorithm 7 Success");
                tvTitle8.setText("Algorithm 8 Success");
                tvTitle9.setText("Algorithm 9 Success");
                tvTitle10.setText("Algorithm 10 Success");
                tvTitle11.setText("Algorithm 11 Success");
                tvTitle12.setText("Algorithm 12 Success");
                tvTitle13.setText("Algorithm 13 Success");

                tvTitleAn1.setText("Rate win ~ 1%");
                tvTitleAn2.setText("Rate win ~ 2%");
                tvTitleAn3.setText("Rate win ~ 3%");
                tvTitleAn4.setText("Rate win ~ 4%");
                tvTitleAn5.setText("Rate win ~ 5%");
                tvTitleAn6.setText("Rate win > 6%");

                tvTitleThua1.setText("Rate lost ~ 0.5%");
                tvTitleThua2.setText("Rate lost ~ 1%");
                tvTitleThua3.setText("Rate lost > 3%");
                tvTitleThua4.setText("Rate lost > 5%");
            }


            tvTyLe1.setText(an1 + " = "
                    + (case1_vol1 > 0 ? case1_vol1 + "*V1" + "|" : "")
                    + (case1_vol2 > 0 ? case1_vol2 + "*V2" + "|" : "")
                    + (case1_vol3 > 0 ? case1_vol3 + "*V3" + "|" : "")
                    + (case1_vol4 > 0 ? case1_vol4 + "*V4" + "|" : "")
                    + (case1_vol5 > 0 ? case1_vol5 + "*V5" + "|" : "")
                    + (case1_vol6 > 0 ? case1_vol6 + "*V6" + "|" : "")
                    + (case1_vol10 > 0 ? case1_vol10 + "*V10" + "|" : ""));
            tvTyLe2.setText(an2 + " = "
                    + (case2_vol1 > 0 ? case2_vol1 + "*V1" + "|" : "")
                    + (case2_vol2 > 0 ? case2_vol2 + "*V2" + "|" : "")
                    + (case2_vol3 > 0 ? case2_vol3 + "*V3" + "|" : "")
                    + (case2_vol4 > 0 ? case2_vol4 + "*V4" + "|" : "")
                    + (case2_vol5 > 0 ? case2_vol5 + "*V5" + "|" : "")
                    + (case2_vol6 > 0 ? case2_vol6 + "*V6" + "|" : "")
                    + (case2_vol10 > 0 ? case2_vol10 + "*V10" + "|" : ""));
            tvTyLe3.setText(an3 + " = "
                    + (case3_vol1 > 0 ? case3_vol1 + "*V1" + "|" : "")
                    + (case3_vol2 > 0 ? case3_vol2 + "*V2" + "|" : "")
                    + (case3_vol3 > 0 ? case3_vol3 + "*V3" + "|" : "")
                    + (case3_vol4 > 0 ? case3_vol4 + "*V4" + "|" : "")
                    + (case3_vol5 > 0 ? case3_vol5 + "*V5" + "|" : "")
                    + (case3_vol6 > 0 ? case3_vol6 + "*V6" + "|" : "")
                    + (case3_vol10 > 0 ? case3_vol10 + "*V10" + "|" : ""));
            tvTyLe4.setText(an4 + " = "
                    + (case4_vol1 > 0 ? case4_vol1 + "*V1" + "|" : "")
                    + (case4_vol2 > 0 ? case4_vol2 + "*V2" + "|" : "")
                    + (case4_vol3 > 0 ? case4_vol3 + "*V3" + "|" : "")
                    + (case4_vol4 > 0 ? case4_vol4 + "*V4" + "|" : "")
                    + (case4_vol5 > 0 ? case4_vol5 + "*V5" + "|" : "")
                    + (case4_vol6 > 0 ? case4_vol6 + "*V6" + "|" : "")
                    + (case4_vol10 > 0 ? case4_vol10 + "*V10" + "|" : ""));
            tvTyLe5.setText(an5 + " = "
                    + (case5_vol1 > 0 ? case5_vol1 + "*V1" + "|" : "")
                    + (case5_vol2 > 0 ? case5_vol2 + "*V2" + "|" : "")
                    + (case5_vol3 > 0 ? case5_vol3 + "*V3" + "|" : "")
                    + (case5_vol4 > 0 ? case5_vol4 + "*V4" + "|" : "")
                    + (case5_vol5 > 0 ? case5_vol5 + "*V5" + "|" : "")
                    + (case5_vol6 > 0 ? case5_vol6 + "*V6" + "|" : "")
                    + (case5_vol10 > 0 ? case5_vol10 + "*V10" + "|" : ""));
            tvTyLe6.setText(an6 + " = "
                    + (case6_vol1 > 0 ? case6_vol1 + "*V1" + "|" : "")
                    + (case6_vol2 > 0 ? case6_vol2 + "*V2" + "|" : "")
                    + (case6_vol3 > 0 ? case6_vol3 + "*V3" + "|" : "")
                    + (case6_vol4 > 0 ? case6_vol4 + "*V4" + "|" : "")
                    + (case6_vol5 > 0 ? case6_vol5 + "*V5" + "|" : "")
                    + (case6_vol6 > 0 ? case6_vol6 + "*V6" + "|" : "")
                    + (case6_vol10 > 0 ? case6_vol10 + "*V10" + "|" : ""));
        } catch (Exception e) {
            Log.e("Ex", e.getMessage());
            e.printStackTrace();
        }
    }

    public void initUI() {
        tvThangThua = (TextView) findViewById(R.id.textView2);
        tvThangNua = (TextView) findViewById(R.id.textView40);
        tvTT1 = (TextView) findViewById(R.id.textView4);
        tvTT2 = (TextView) findViewById(R.id.textView6);
        tvTT3 = (TextView) findViewById(R.id.textView8);
        tvTT4 = (TextView) findViewById(R.id.textView10);
        tvTT5 = (TextView) findViewById(R.id.textView12);
        tvTT6 = (TextView) findViewById(R.id.textView14);
        tvTT7 = (TextView) findViewById(R.id.textView16);
        tvTT8 = (TextView) findViewById(R.id.textView18);
        tvTT9 = (TextView) findViewById(R.id.textView20);
        tvTT10 = (TextView) findViewById(R.id.textView22);
        tvTyLe1 = (TextView) findViewById(R.id.textView24);
        tvTyLe2 = (TextView) findViewById(R.id.textView26);
        tvTyLe3 = (TextView) findViewById(R.id.textView28);
        tvTyLe4 = (TextView) findViewById(R.id.textView30);
        tvTyLe5 = (TextView) findViewById(R.id.textView32);
        tvTyLe6 = (TextView) findViewById(R.id.textView34);
        tvTT12 = (TextView) findViewById(R.id.textView38);
        tvTT11 = (TextView) findViewById(R.id.textView36);
        tvTT13 = (TextView) findViewById(R.id.textView50);

//        tvThua1 = (TextView) findViewById(R.id.textView42);
//        tvThua2 = (TextView) findViewById(R.id.textView44);
//        tvThua3 = (TextView) findViewById(R.id.textView46);

        tvTitle1 = (TextView) findViewById(R.id.textView3);
        tvTitle2 = (TextView) findViewById(R.id.textView5);
        tvTitle3 = (TextView) findViewById(R.id.textView7);
        tvTitle4 = (TextView) findViewById(R.id.textView9);
        tvTitle5 = (TextView) findViewById(R.id.textView11);
        tvTitle6 = (TextView) findViewById(R.id.textView13);
        tvTitle7 = (TextView) findViewById(R.id.textView15);
        tvTitle8 = (TextView) findViewById(R.id.textView17);
        tvTitle9 = (TextView) findViewById(R.id.textView19);
        tvTitle10 = (TextView) findViewById(R.id.textView21);
        tvTitle11 = (TextView) findViewById(R.id.textView35);
        tvTitle12 = (TextView) findViewById(R.id.textView37);
        tvTitle13 = (TextView) findViewById(R.id.textView49);
        tvTitleThang1 = (TextView) findViewById(R.id.textView39);
        tvTitleThang2 = (TextView) findViewById(R.id.textView1);
        tvTitleAn1 = (TextView) findViewById(R.id.textView23);
        tvTitleAn2 = (TextView) findViewById(R.id.textView25);
        tvTitleAn3 = (TextView) findViewById(R.id.textView27);
        tvTitleAn4 = (TextView) findViewById(R.id.textView29);
        tvTitleAn5 = (TextView) findViewById(R.id.textView31);
        tvTitleAn6 = (TextView) findViewById(R.id.textView33);

        tvTitleThua1 = (TextView) findViewById(R.id.textView41);
        tvTitleThua2 = (TextView) findViewById(R.id.textView43);
        tvTitleThua3 = (TextView) findViewById(R.id.textView45);
        tvTitleThua4 = (TextView) findViewById(R.id.textView47);

    }

    public Double subGetGiaMin(JSONArray arr) {
        try {
            Double giaMin = 100D;
            for (int i = 0; i < arr.length(); i++) {
                JSONArray arrItem = arr.getJSONArray(i);
                Double giaTime = Double.parseDouble(arrItem.getString(2));
                if (giaTime < giaMin) {
                    giaMin = giaTime;
                }
            }
            return giaMin;
        } catch (Exception e) {

        }
        return 0D;
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public String readJsonFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return jsonText;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            is.close();
        }
        return "";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
