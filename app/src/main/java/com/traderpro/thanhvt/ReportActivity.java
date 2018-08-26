package com.traderpro.thanhvt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.traderpro.GCM.Config;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    TextView tvThangThua, tvTT1, tvTT2, tvTT3, tvTT4, tvTT5, tvTT6, tvTT7, tvTT8, tvTT9, tvTT10, tvTT11, tvTT12;
    TextView tvTyLe1, tvTyLe2, tvTyLe3, tvTyLe4, tvTyLe5, tvTyLe6;
    TextView tvThangNua;

    TextView tvThua1, tvThua2, tvThua3;

    TextView tvTitle1,tvTitle2,tvTitle3,tvTitle4,tvTitle5,tvTitle6,tvTitle7,tvTitle8,tvTitle9,tvTitle10,tvTitle11,tvTitle12;
    TextView tvTitleThang1,tvTitleThang2;
    TextView tvTitleAn1,tvTitleAn2,tvTitleAn3,tvTitleAn4,tvTitleAn5,tvTitleAn6;
    TextView tvTitleThua1,tvTitleThua2,tvTitleThua3,tvTitleThua4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

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
        ((AppCompatActivity) this).getSupportActionBar().setTitle("Trading Statistics " + strTime);

        int countThangThua = 0, countThangNua = 0;
        int tong1 = 0, thang1 = 0, tong2 = 0, thang2 = 0, tong3 = 0, thang3 = 0, tong4 = 0, thang4 = 0, tong5 = 0, thang5 = 0,
                tong6 = 0, thang6 = 0, tong7 = 0, thang7 = 0, tong8 = 0, thang8 = 0, tong9 = 0, thang9 = 0,
                tong10 = 0, thang10 = 0, tong11 = 0, thang11 = 0, tong12 = 0, thang12 = 0;
        int an1 = 0, an2 = 0, an3 = 0, an4 = 0, an5 = 0, an6 = 0;
        int case1_vol1 = 0, case1_vol2 = 0, case1_vol3 = 0, case1_vol4 = 0, case1_vol5 = 0, case1_vol6 = 0;
        int case2_vol1 = 0, case2_vol2 = 0, case2_vol3 = 0, case2_vol4 = 0, case2_vol5 = 0, case2_vol6 = 0;
        int case3_vol1 = 0, case3_vol2 = 0, case3_vol3 = 0, case3_vol4 = 0, case3_vol5 = 0, case3_vol6 = 0;
        int case4_vol1 = 0, case4_vol2 = 0, case4_vol3 = 0, case4_vol4 = 0, case4_vol5 = 0, case4_vol6 = 0;
        int case5_vol1 = 0, case5_vol2 = 0, case5_vol3 = 0, case5_vol4 = 0, case5_vol5 = 0, case5_vol6 = 0;
        int case6_vol1 = 0, case6_vol2 = 0, case6_vol3 = 0, case6_vol4 = 0, case6_vol5 = 0, case6_vol6 = 0;

        int countThua1 =0, countThua2 = 0, countThua3 = 0;

        for (NotificationEntity en : lstEntity) {
            Double giaBao = Double.parseDouble(en.strGia);
            Double giaMax = en.strGiaMax;
            Double giaMin = en.strGiaMin;
            Double volHT = Double.parseDouble(en.strVol);
            Double volTB = Double.parseDouble(en.strVolTB);

            //
            if(giaMin != null){
                if(giaMin < giaBao * 0.995){
                    countThua1++;
                }
                if(giaMin < giaBao * 0.99){
                    countThua2++;
                }
                if(giaMin < giaBao * 0.97){
                    countThua3++;
                }
            }

            //
            if (giaMax > giaBao * 1.005) {
                countThangNua++;
            }
            if (giaMax > giaBao * 1.01) {
                countThangThua++;

                if (en.strCase.equals("1")) {
                    thang1++;
                }
                if (en.strCase.equals("2")) {
                    thang2++;
                }
                if (en.strCase.equals("3")) {
                    thang3++;
                }
                if (en.strCase.equals("4")) {
                    thang4++;
                }
                if (en.strCase.equals("5")) {
                    thang5++;
                }
                if (en.strCase.equals("6")) {
                    thang6++;
                }
                if (en.strCase.equals("7")) {
                    thang7++;
                }
                if (en.strCase.equals("8")) {
                    thang8++;
                }
                if (en.strCase.equals("9")) {
                    thang9++;
                }
                if (en.strCase.equals("10")) {
                    thang10++;
                }
                if (en.strCase.equals("11")) {
                    thang11++;
                }
                if (en.strCase.equals("12")) {
                    thang12++;
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
                } else if (volTB * 6 <= volHT) {
                    case1_vol6++;
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
                } else if (volTB * 6 <= volHT) {
                    case2_vol6++;
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
                } else if (volTB * 6 <= volHT) {
                    case3_vol6++;
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
                } else if (volTB * 6 <= volHT) {
                    case4_vol6++;
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
                } else if (volTB * 6 <= volHT) {
                    case5_vol6++;
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
                } else if (volTB * 6 <= volHT) {
                    case6_vol6++;
                }
            }
        }

        SharedPreferences pref = getApplication().getSharedPreferences(Config.NGON_NGU, 0);
        String strNN = pref.getString("NN", "VN");

        countThua1 = countThua1 / 4;
        countThua2 = countThua2 / 4;
        countThua3 = countThua3 / 4;
        if(strNN.equalsIgnoreCase("VN")){
            tvThua1.setText(countThua1 + " lần");
            tvThua2.setText(countThua2 + " lần");
            tvThua3.setText(countThua3 + " lần");

            tvThangThua.setText(countThangThua + "/" + strTinhToan + " lần báo");
            tvThangNua.setText(countThangNua + "/" + strTinhToan + " lần báo");
            tvTT1.setText(thang1 + "/" + tong1 + " lần báo");
            tvTT2.setText(thang2 + "/" + tong2 + " lần báo");
            tvTT3.setText(thang3 + "/" + tong3 + " lần báo");
            tvTT4.setText(thang4 + "/" + tong4 + " lần báo");
            tvTT5.setText(thang5 + "/" + tong5 + " lần báo");
            tvTT6.setText(thang6 + "/" + tong6 + " lần báo");
            tvTT7.setText(thang7 + "/" + tong7 + " lần báo");
            tvTT8.setText(thang8 + "/" + tong8 + " lần báo");
            tvTT9.setText(thang9 + "/" + tong9 + " lần báo");
            tvTT10.setText(thang10 + "/" + tong10 + " lần báo");
            tvTT11.setText(thang11 + "/" + tong11 + " lần báo");
            tvTT12.setText(thang12 + "/" + tong12 + " lần báo");

            tvTitleThang1.setText("Tỷ lệ thắng > 0.5%");
            tvTitleThang2.setText("Tỷ lệ thắng > 1%");
            tvTitle1.setText("Thuật toán 1 thành công");
            tvTitle2.setText("Thuật toán 2 thành công");
            tvTitle3.setText("Thuật toán 3 thành công");
            tvTitle4.setText("Thuật toán 4 thành công");
            tvTitle5.setText("Thuật toán 5 thành công");
            tvTitle6.setText("Thuật toán 6 thành công");
            tvTitle7.setText("Thuật toán 7 thành công");
            tvTitle8.setText("Thuật toán 8 thành công");
            tvTitle9.setText("Thuật toán 9 thành công");
            tvTitle10.setText("Thuật toán 10 thành công");
            tvTitle11.setText("Thuật toán 11 thành công");
            tvTitle12.setText("Thuật toán 12 thành công");

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
        }else{
            tvThua1.setText(countThua1 + " times");
            tvThua2.setText(countThua2 + " times");
            tvThua3.setText(countThua3 + " times");

            tvThangThua.setText(countThangThua + "/" + strTinhToan + " note times");
            tvThangNua.setText(countThangNua + "/" + strTinhToan + " note times");
            tvTT1.setText(thang1 + "/" + tong1 + " note times");
            tvTT2.setText(thang2 + "/" + tong2 + " note times");
            tvTT3.setText(thang3 + "/" + tong3 + " note times");
            tvTT4.setText(thang4 + "/" + tong4 + " note times");
            tvTT5.setText(thang5 + "/" + tong5 + " note times");
            tvTT6.setText(thang6 + "/" + tong6 + " note times");
            tvTT7.setText(thang7 + "/" + tong7 + " note times");
            tvTT8.setText(thang8 + "/" + tong8 + " note times");
            tvTT9.setText(thang9 + "/" + tong9 + " note times");
            tvTT10.setText(thang10 + "/" + tong10 + " note times");
            tvTT11.setText(thang11 + "/" + tong11 + " note times");
            tvTT12.setText(thang12 + "/" + tong12 + " note times");

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
                + (case1_vol6 > 0 ? case1_vol6 + "*V6" + "|" : ""));
        tvTyLe2.setText(an2 + " = "
                + (case2_vol1 > 0 ? case2_vol1 + "*V1" + "|" : "")
                + (case2_vol2 > 0 ? case2_vol2 + "*V2" + "|" : "")
                + (case2_vol3 > 0 ? case2_vol3 + "*V3" + "|" : "")
                + (case2_vol4 > 0 ? case2_vol4 + "*V4" + "|" : "")
                + (case2_vol5 > 0 ? case2_vol5 + "*V5" + "|" : "")
                + (case2_vol6 > 0 ? case2_vol6 + "*V6" + "|" : ""));
        tvTyLe3.setText(an3 + " = "
                + (case3_vol1 > 0 ? case3_vol1 + "*V1" + "|" : "")
                + (case3_vol2 > 0 ? case3_vol2 + "*V2" + "|" : "")
                + (case3_vol3 > 0 ? case3_vol3 + "*V3" + "|" : "")
                + (case3_vol4 > 0 ? case3_vol4 + "*V4" + "|" : "")
                + (case3_vol5 > 0 ? case3_vol5 + "*V5" + "|" : "")
                + (case3_vol6 > 0 ? case3_vol6 + "*V6" + "|" : ""));
        tvTyLe4.setText(an4 + " = "
                + (case4_vol1 > 0 ? case4_vol1 + "*V1" + "|" : "")
                + (case4_vol2 > 0 ? case4_vol2 + "*V2" + "|" : "")
                + (case4_vol3 > 0 ? case4_vol3 + "*V3" + "|" : "")
                + (case4_vol4 > 0 ? case4_vol4 + "*V4" + "|" : "")
                + (case4_vol5 > 0 ? case4_vol5 + "*V5" + "|" : "")
                + (case4_vol6 > 0 ? case4_vol6 + "*V6" + "|" : ""));
        tvTyLe5.setText(an5 + " = "
                + (case5_vol1 > 0 ? case5_vol1 + "*V1" + "|" : "")
                + (case5_vol2 > 0 ? case5_vol2 + "*V2" + "|" : "")
                + (case5_vol3 > 0 ? case5_vol3 + "*V3" + "|" : "")
                + (case5_vol4 > 0 ? case5_vol4 + "*V4" + "|" : "")
                + (case5_vol5 > 0 ? case5_vol5 + "*V5" + "|" : "")
                + (case5_vol6 > 0 ? case5_vol6 + "*V6" + "|" : ""));
        tvTyLe6.setText(an6 + " = "
                + (case6_vol1 > 0 ? case6_vol1 + "*V1" + "|" : "")
                + (case6_vol2 > 0 ? case6_vol2 + "*V2" + "|" : "")
                + (case6_vol3 > 0 ? case6_vol3 + "*V3" + "|" : "")
                + (case6_vol4 > 0 ? case6_vol4 + "*V4" + "|" : "")
                + (case6_vol5 > 0 ? case6_vol5 + "*V5" + "|" : "")
                + (case6_vol6 > 0 ? case6_vol6 + "*V6" + "|" : ""));
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

        tvThua1 = (TextView) findViewById(R.id.textView42);
        tvThua2 = (TextView) findViewById(R.id.textView44);
        tvThua3 = (TextView) findViewById(R.id.textView46);

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
