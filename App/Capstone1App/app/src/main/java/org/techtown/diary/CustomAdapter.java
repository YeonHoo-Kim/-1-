package org.techtown.diary;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by oshin on 1/30/2018.
 */

class CustomAdapter extends BaseAdapter{

    private Context applicationContext;
    private int sample;
    private List<JsonModel> jsonModels;


    CustomAdapter(Context applicationContext, int sample, List<JsonModel> jsonModels) {

        this.applicationContext =applicationContext;
        this.sample = sample;
        this.jsonModels =jsonModels;

    }


    @Override
    public int getCount() {
        return jsonModels.size();
    }

    @Override
    public Object getItem(int i) {
        return jsonModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {



        if(view == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view =  layoutInflater.inflate(R.layout.sample,viewGroup,false);

        }

        TextView id,car_number,date_time,count;

        id= view.findViewById(R.id.s1);
        car_number=view.findViewById(R.id.s2);
        date_time=view.findViewById(R.id.s3);
        count=view.findViewById(R.id.s4);

        id.setText( Integer.toString(jsonModels.get(i).getID()));
        car_number.setText(jsonModels.get(i).getCAR_NUMBER());
        date_time.setText(jsonModels.get(i).getDATE_TIME());
        count.setText(jsonModels.get(i).get1COUNT());
        return view;
    }
}