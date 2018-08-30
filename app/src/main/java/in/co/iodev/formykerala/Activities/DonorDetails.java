package in.co.iodev.formykerala.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.co.iodev.formykerala.Controllers.CheckInternet;
import in.co.iodev.formykerala.Controllers.HTTPGet;
import in.co.iodev.formykerala.Controllers.HTTPPostGet;
import in.co.iodev.formykerala.Controllers.ProgressBarHider;
import in.co.iodev.formykerala.Models.DataModel;
import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static in.co.iodev.formykerala.Constants.Constants.Get_District;
import static in.co.iodev.formykerala.Constants.Constants.Register_Donors;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class DonorDetails extends AppCompatActivity {
    EditText name,address;
    Spinner district,taluk;
    JSONObject object;
    String Name,District,Taluk;
    Gson gson = new Gson();
    Button next,logout;
    String StringData;
    ImageView back;
    SharedPreferences sharedPref;
    String request_post_url=Register_Donors,url2=Get_District,TimeIndex;
    Context context;
    ArrayAdapter<String> adapter;
    ProgressBarHider hider;
    Boolean submit=false;

    ArrayList<String> districts;
    ArrayList<String> taluks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_details);
        districts=new ArrayList<String>();
        new HTTPAsyncTask2().execute(url2);
        name=findViewById(R.id.name);
        address=findViewById(R.id.address);
        district=findViewById(R.id.district);
        taluk=findViewById(R.id.taluk);
        next=findViewById(R.id.button6);
        logout=findViewById(R.id.logout);
        back=findViewById(R.id.back_button);
        hider=new ProgressBarHider(next.getRootView(),next);
        sharedPref=getDefaultSharedPreferences(getApplicationContext());
        TimeIndex=sharedPref.getString("TimeIndex","");
        context=this;
        adapter= new ArrayAdapter<String>(this,
                R.layout.spinner_layout, districts);
        adapter.setDropDownViewResource(R.layout.drop_down_tems);
        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                taluks = new ArrayList<>();
                try {
                    JSONArray array=new JSONObject(object.toString()).getJSONArray(district.getSelectedItem().toString());
                    Log.d("array1",array.toString());
                    for (int j=0; j<array.length(); j++) {
                        taluks.add( array.getString(j) );

                    }
                    Log.d("array1",taluks.toString());

                }catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.spinner_layout, taluks);
                adapter.setDropDownViewResource(R.layout.drop_down_tems);
                taluk.setAdapter(adapter1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(TimeIndex+"DLogin",FALSE);
                editor.remove("TimeIndex");
              /*  editor.putBoolean("Edited",FALSE);
                editor.putBoolean("EditedR",FALSE);
*/
                editor.commit();
                sharedPref.edit().apply();

                startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();

            }

        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("jisjoe","jisjoe");
                Name=name.getText().toString();

                District=district.getSelectedItem().toString();
                Taluk=taluk.getSelectedItem().toString();
                /*District=district.getText().toString();
                Taluk=taluk.getText().toString();*/
                if(Name.equals("")){
                    Toast.makeText(getApplicationContext(),"Please provide all fields", Toast.LENGTH_LONG).show();
                }
                else {

                    DataModel d=new DataModel();
                    d.setName(Name);
                    d.setDistrict(District);
                    d.setTaluk(Taluk);
                    d.setTimeIndex(TimeIndex);
                    StringData=gson.toJson(d);
                    Log.i("jisjoe",""+StringData);
                    submit=true;
                    hider.show();
                    new HTTPAsyncTask2().execute(request_post_url);


                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void next(View view) {


    }
    private class HTTPAsyncTask2 extends AsyncTask<String, Void, String> {
        String response="Network Error";
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    if(!submit)
                        response= HTTPGet.getJsonResponse(url2);
                    else
                        response= HTTPPostGet.getJsonResponse(urls[0],StringData);
                    Log.i("jisjoe",response.toString());
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Error!";
                }
                finally {
                    hider.hide();
                }
            } catch (Exception e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        @Override
        protected void onPreExecute() {
            CheckInternet CI=new CheckInternet();
            CI.isOnline(context);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            try {JSONObject responseObject=new JSONObject(response);
                hider.hide();
                if(!submit)
                {
                    object=new JSONObject(response);
                    Log.d("Array", String.valueOf(object));

                    Iterator<String> iter = object.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        districts.add(key);

                        try {
                            Log.d("taluk",object.get(key).toString());
                        } catch (JSONException e) {
                            // Something went wrong!
                        }
                        Log.d("iter",districts.toString());
                    }
                    district.setAdapter(adapter);
                }
                else
                {

                    Toast.makeText(getApplicationContext(),responseObject.getString("Message"),Toast.LENGTH_LONG).show();

                Toast.makeText(getApplicationContext(),responseObject.getString("Message"),Toast.LENGTH_LONG).show();
                if(responseObject.getString("Message").equals("Success")) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(TimeIndex+"DEdited", TRUE);
                    editor.apply();
                    Intent intent = new Intent(DonorDetails.this, DonorSelectItems.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    finish();
                }}}
             catch (JSONException e) {
                e.printStackTrace();}
        }}




    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}


