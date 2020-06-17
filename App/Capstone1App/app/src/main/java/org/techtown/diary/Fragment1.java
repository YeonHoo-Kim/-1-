package org.techtown.diary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Fragment1 extends Fragment {

    private int getFlag;
    //private String getDate;
    private TextView textView;  // state
    private WebView webView1;    // getdata1
    private WebView webView2;    // getdata2
    private WebView webView3;   // enrollCar
    private WebView webView4;   // blackCar
    private WebView mWebView;   // car streaming
    private WebSettings mWebSettings; //웹뷰세팅
    private Button searchButton;
    private Button report;
    private ListView carList;
    private ListViewAdapter adapter = null;
    static int counter = 0;
    private final Handler handler = new Handler();
    private org.json.JSONArray enrollCar;
    private org.json.JSONArray blackCar;

    Context context;
    OnTabItemSelectedListener listener;
    protected FragmentActivity activity;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;

        if (context instanceof OnTabItemSelectedListener) {
            listener = (OnTabItemSelectedListener) context;
        }
        if(context instanceof Activity){
            activity = (FragmentActivity)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (context != null) {
            context = null;
            listener = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);
        initUI(rootView);
        return rootView;
    }

    private void initUI(ViewGroup rootView){

        textView = rootView.findViewById(R.id.state);
        mWebView = rootView.findViewById(R.id.carStreaming);
        mWebView.setWebViewClient(new WebViewClient());
        mWebSettings = mWebView.getSettings(); //세부 세팅 등록
        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setSupportZoom(true); // 화면 줌 허용 여부
        mWebSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("http://ec2-34-229-114-134.compute-1.amazonaws.com:8000/stream");
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);

        webView1 = rootView.findViewById(R.id.getdata1);
        webView2 = rootView.findViewById(R.id.getdata2);
        webView3 = rootView.findViewById(R.id.enrollcar);
        webView4 = rootView.findViewById(R.id.blackcar);
        webView1.loadUrl("http://ec2-34-229-114-134.compute-1.amazonaws.com:8082/hc");
        webView2.loadUrl("http://ec2-34-229-114-134.compute-1.amazonaws.com:8083/searchcar");
        webView3.loadUrl("http://ec2-34-229-114-134.compute-1.amazonaws.com:8083/searchenroll");
        webView4.loadUrl("http://ec2-34-229-114-134.compute-1.amazonaws.com:8083/searchblack");

        searchButton = rootView.findViewById(R.id.search);
        report = rootView.findViewById(R.id.report);
        carList = rootView.findViewById(R.id.listview);

        String[] from = new String[]{"차량번호", "주차시간", "주소"};
        int[] to = new int[] {R.id.head1, R.id.head2, R.id.head3};
        adapter = new ListViewAdapter();
        carList.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Update4();
                Update3();
                Update2();
                adapter.notifyDataSetChanged();
            }
        });

        report.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://smartreport.seoul.go.kr/"));
                startActivity(intent);
            }
        });

        TimerTask tt = new TimerTask() {
            @Override
            public void run(){
                Log.e("test : ",String.valueOf(counter));
                Update1();
                if(getFlag == 1){
                    textView.setText("현재 주차장 상황 : 사용중");
                    textView.setTextColor(Color.parseColor("#FF0000"));
                    ((MainActivity)getActivity()).v2flag = 1;
                    ((MainActivity)getActivity()).s2flag = 1;
                    ((MainActivity)getActivity()).m2flag = 1;

                    if(((MainActivity)getActivity()).v1flag == 1 && ((MainActivity)getActivity()).v2flag == 1){
                        Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
                        if(Build.VERSION.SDK_INT>=26){
                            vibrator.vibrate(VibrationEffect.createOneShot(1000,10));
                        }else{
                            vibrator.vibrate(1000);
                        }
                    }
                    if(((MainActivity)getActivity()).s1flag ==1 && ((MainActivity)getActivity()).s2flag ==1){
                        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone ringtone = RingtoneManager.getRingtone(activity.getApplicationContext(), uri);
                        ringtone.play();
                    }
                    if(((MainActivity)getActivity()).m1flag ==1 && ((MainActivity)getActivity()).m2flag ==1){
                        ((MainActivity)activity).showNoti1();
                    }
                } else {
                    textView.setText("현재 주차장 상황 : 사용가능");
                    textView.setTextColor(Color.parseColor("#00FFE6"));
                    ((MainActivity)getActivity()).s2flag = 0;
                    ((MainActivity)getActivity()).m2flag = 0;
                    ((MainActivity)getActivity()).v2flag = 0;
                }
                counter++;
            }
        };

        Timer timer = new Timer();
        timer.schedule(tt,0,1000);
    }

    protected void Update1() {
        Runnable updater = new Runnable() {
            public void run() {
                webView1.getSettings().setJavaScriptEnabled(true); //Javascript를 사용하도록 설정
                webView1.addJavascriptInterface(new MyJavascriptInterface1(), "Android");

                webView1.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        view.loadUrl("javascript:window.Android.getHtml(document.body.innerText);"); //<html></html> 사이에 있는 모든 html을 넘겨준다.
                    }
                });

                webView1.loadUrl("http://ec2-34-229-114-134.compute-1.amazonaws.com:8082/hc");
            }
        };
        handler.post(updater);
    }

    public class MyJavascriptInterface1 {

        @JavascriptInterface
        public void getHtml(String html) throws JSONException { //위 자바스크립트가 호출되면 여기로 html이 반환됨
            org.json.JSONArray jsonArray = new org.json.JSONArray(html);
            try{
                org.json.JSONObject jsonObj = (org.json.JSONObject) jsonArray.get(0);
                String flag = jsonObj.getString("flag");
                String date = jsonObj.getString("date");
                getFlag = Integer.parseInt(flag);
                //getDate = date;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    protected void Update2() {
        Runnable updater = new Runnable() {
            public void run() {
                webView2.getSettings().setJavaScriptEnabled(true); //Javascript를 사용하도록 설정
                webView2.addJavascriptInterface(new MyJavascriptInterface2(), "Android");
                webView2.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        view.loadUrl("javascript:window.Android.getHtml(document.body.innerText);"); //<html></html> 사이에 있는 모든 html을 넘겨준다.
                    }
                });
                webView2.loadUrl("http://ec2-34-229-114-134.compute-1.amazonaws.com:8083/searchcar");
            }
        };
        handler.post(updater);
    }

    public class MyJavascriptInterface2 {

        @JavascriptInterface
        public void getHtml(String html) throws JSONException { //위 자바스크립트가 호출되면 여기로 html이 반환됨
            org.json.JSONArray jsonArray = new org.json.JSONArray(html);
            try{
                adapter.clearItem();
                for (int i = 0; i < jsonArray.length(); i++) {
                    org.json.JSONObject carInfo = (org.json.JSONObject) jsonArray.get(i);
                    String car_number = carInfo.getString("car_number");
                    String parking_date_time = carInfo.getString("parking_date_time");
                    String loc = carInfo.getString("loc");
                    String tmp1;
                    int flag = 0;
                    for (int j = 0; j < blackCar.length(); j++) {
                        org.json.JSONObject tmp = (org.json.JSONObject) blackCar.get(j);
                        tmp1 = tmp.getString("car_number");
                        Log.d("BlackList : ", tmp1);
                        if (car_number.equals(tmp1)) {
                            adapter.addItem(R.drawable.car2, car_number, parking_date_time+'\t'+loc);
                            //Log.d("BlackList : ", tmp1);
                            flag = 1;
                            break;
                        }
                    }
                    if(flag == 0) {
                        for (int j = 0; j < enrollCar.length(); j++) {
                            org.json.JSONObject tmp = (org.json.JSONObject) enrollCar.get(j);
                            tmp1 = tmp.getString("car_number");
                            //Log.d("EnrollList : ", tmp1);
                            if (car_number.equals(tmp1)) {
                                adapter.addItem(R.drawable.car1, car_number, parking_date_time + '\t' + loc);
                                flag = 1;
                                //Log.d("EnrollList : ", tmp1);
                                break;
                            }
                        }
                    } else {
                        continue;
                    }
                    if(flag == 0){
                        adapter.addItem(R.drawable.car3, car_number, parking_date_time + '\t' + loc);
                        //Log.d("Common : ", car_number);
                    } else {
                        continue;
                    }
                    //Toast.makeText(getContext(), "carInfo " + carinfoMap, Toast.LENGTH_SHORT).show();
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    protected void Update3() {
        Runnable updater = new Runnable() {
            public void run() {
                webView3.getSettings().setJavaScriptEnabled(true); //Javascript를 사용하도록 설정
                webView3.addJavascriptInterface(new MyJavascriptInterface3(), "Android");
                webView3.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        view.loadUrl("javascript:window.Android.getHtml(document.body.innerText);"); //<html></html> 사이에 있는 모든 html을 넘겨준다.
                    }
                });
                webView3.loadUrl("http://ec2-34-229-114-134.compute-1.amazonaws.com:8083/searchenroll");
            }
        };
        handler.post(updater);
    }

    public class MyJavascriptInterface3 {

        @JavascriptInterface
        public void getHtml(String html) throws JSONException { //위 자바스크립트가 호출되면 여기로 html이 반환됨
            org.json.JSONArray jsonArray = new org.json.JSONArray(html);
            enrollCar = jsonArray;
        }
    }
    protected void Update4() {
        Runnable updater = new Runnable() {
            public void run() {
                webView4.getSettings().setJavaScriptEnabled(true); //Javascript를 사용하도록 설정
                webView4.addJavascriptInterface(new MyJavascriptInterface4(), "Android");
                webView4.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        view.loadUrl("javascript:window.Android.getHtml(document.body.innerText);"); //<html></html> 사이에 있는 모든 html을 넘겨준다.
                    }
                });
                webView4.loadUrl("http://ec2-34-229-114-134.compute-1.amazonaws.com:8083/searchblack");
            }
        };
        handler.post(updater);
    }

    public class MyJavascriptInterface4 {

        @JavascriptInterface
        public void getHtml(String html) throws JSONException { //위 자바스크립트가 호출되면 여기로 html이 반환됨
            org.json.JSONArray jsonArray = new org.json.JSONArray(html);
            blackCar = jsonArray;
        }
    }
}
