package org.techtown.diary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Fragment2 extends Fragment {

    private ListView l1;
    public JsonWork jsonWork = new JsonWork();
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);
        initUI(rootView);
        return rootView;
    }

    private void initUI(ViewGroup rootView){
        Button button = (Button)rootView.findViewById(R.id.list_add);
        Button button1=(Button)rootView.findViewById(R.id.list_del);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        l1 = (ListView)rootView.findViewById(R.id.l1);

        jsonWork.execute();


    }

    void delete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_delete,null);
        builder.setView(view);
        final Button submit = (Button) view.findViewById(R.id.buttonDelete);
        final EditText email = (EditText) view.findViewById(R.id.aaa);
        final EditText password = (EditText) view.findViewById(R.id.edittextPassword);
        final WebView web_x=(WebView)view.findViewById(R.id.webx);
        WebSettings web_set;
        final AlertDialog dialog = builder.create();
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HttpURLConnection add=null;
                String strEmail = email.getText().toString();
                String strPassword = password.getText().toString();
                //Toast.makeText(getApplicationContext(), strEmail+"/"+strPassword,Toast.LENGTH_LONG).show();
                web_x.loadUrl("http://ec2-34-229-114-134.compute-1.amazonaws.com:8083/delenroll?car_number="+strEmail);
                jsonWork.selfRestart();
                jsonWork.execute();
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_login,null);
        builder.setView(view);
        final Button submit = (Button) view.findViewById(R.id.buttonSubmit);
        final EditText email = (EditText) view.findViewById(R.id.edittextEmailAddress);
        final EditText password = (EditText) view.findViewById(R.id.edittextPassword);
        final WebView web_x=(WebView)view.findViewById(R.id.webx);
        WebSettings web_set;
        final AlertDialog dialog = builder.create();
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HttpURLConnection add=null;
                String strEmail = email.getText().toString();
                String strPassword = password.getText().toString();
                //Toast.makeText(getApplicationContext(), strEmail+"/"+strPassword,Toast.LENGTH_LONG).show();
                web_x.loadUrl("http://ec2-34-229-114-134.compute-1.amazonaws.com:8083/newenroll?car_number="+strEmail+"&name="+strPassword);
                jsonWork.selfRestart();
                jsonWork.execute();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private class JsonWork extends AsyncTask<String,String,List<JsonModel>> {

        //private String TAG="제대로";
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String  FullJsonData;

        protected void onPreExecute() {
            super.onPreExecute();
            jsonWork.selfRestart();
        }

        @Override
        protected List<JsonModel> doInBackground(String... strings) {

            try {
                URL url = new URL("http://ec2-34-229-114-134.compute-1.amazonaws.com:8083/searchenroll");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer= new StringBuffer();
                String line="";

                while ((line= bufferedReader.readLine())!= null){

                    stringBuffer.append(line);

                }
                FullJsonData =  stringBuffer.toString();

                List<JsonModel> jsonModelList = new ArrayList<>();
                //Log.d("POWERPOWER",FullJsonData);
                //JSONObject jsonStartingObject = new JSONObject(FullJsonData);
                //JSONParser parser = new JSONParser();
                //JSONArray jsonStudentArray = (JSONArray)parser.parse(FullJsonData);
                JSONArray jsonStudentArray=new JSONArray(FullJsonData);


                for(int i=0; i<jsonStudentArray.length(); i++) {

                    JSONObject jsonUnderArrayObject = jsonStudentArray.getJSONObject(i);

                    JsonModel jsonModel = new JsonModel();
                    jsonModel.setID(i+1);
                    jsonModel.setCAR_NUMBER(jsonUnderArrayObject.getString("car_number"));
                    jsonModel.setDATE_TIME(jsonUnderArrayObject.getString("enrolled_date_time"));
                    jsonModel.setCOUNT(jsonUnderArrayObject.getString("name"));
                    jsonModelList.add(jsonModel);
                }
                //Collections.reverse(jsonModelList);
                return jsonModelList;


            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally{

                httpURLConnection.disconnect();
                try {
                    if(bufferedReader != null){
                        bufferedReader.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }
        public void selfRestart() {
            jsonWork= new JsonWork();
        }

        @Override
        protected void onPostExecute(List<JsonModel> jsonModels) {
            super.onPostExecute(jsonModels);
            CustomAdapter adapter = new CustomAdapter(activity.getApplicationContext(),R.layout.sample,jsonModels);
            l1.setAdapter(adapter);

        }
    }




}