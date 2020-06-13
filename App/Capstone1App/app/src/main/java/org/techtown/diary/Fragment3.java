package org.techtown.diary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.net.HttpURLConnection;

public class Fragment3 extends Fragment {

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment3, container, false);
        initUI(rootView);
        return rootView;
    }

    private void initUI(ViewGroup rootView){
        Switch switch1 = (Switch)rootView.findViewById(R.id.switch1);
        Switch switch2 = (Switch)rootView.findViewById(R.id.switch2);
        Switch switch3 = (Switch)rootView.findViewById(R.id.switch3);
        Button button = (Button)rootView.findViewById(R.id.button);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ((MainActivity)getActivity()).v1flag=1;
                }
                else{
                    ((MainActivity)getActivity()).v1flag=0;
                }
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ((MainActivity)getActivity()).s1flag =1;
                }
                else{
                    ((MainActivity)getActivity()).s1flag=0;
                }
            }
        });

        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ((MainActivity)getActivity()).m1flag =1;
                }
                else{
                    ((MainActivity)getActivity()).m1flag =0;
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

    }

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.locset,null);
        builder.setView(view);
        final Button b1 = (Button) view.findViewById(R.id.button_submit);
        final EditText em = (EditText) view.findViewById(R.id.edittextloc);
        final WebView wx=(WebView)view.findViewById(R.id.websee);
        WebSettings web_set;
        final AlertDialog dialog = builder.create();
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HttpURLConnection add=null;
                String strem = em.getText().toString();
                wx.loadUrl("http://ec2-34-229-114-134.compute-1.amazonaws.com:8083/changeloc?loc="+strem);
                dialog.dismiss();
            }
        });

        dialog.show();

    }

}