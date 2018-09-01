package in.co.iodev.formykerala.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import in.co.iodev.formykerala.Controllers.CheckInternet;
import in.co.iodev.formykerala.Controllers.HTTPPostGet;
import in.co.iodev.formykerala.Controllers.ProgressBarHider;
import in.co.iodev.formykerala.Models.DataModel;
import in.co.iodev.formykerala.Models.data;
import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static in.co.iodev.formykerala.Constants.Constants.Generate_OTP;
import static in.co.iodev.formykerala.Constants.Constants.Resend_OTP;

public class OTPVerification extends AppCompatActivity {
    EditText phone;
    Button submit;
    Gson gson = new Gson();
    SharedPreferences sharedPref;
    Boolean flag=true;
    DataModel d;
    ImageView back;
    Context context;
    ProgressBarHider hider;

    String StringData,request_post_url=Generate_OTP,TimeIndex;
    Spinner countryCodeSpinner;
    String countrycode[];
    String code;
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setAppLocale(MainActivity.languagePreferences.getString("LOCALE_CODE", null), getResources());
        setContentView(R.layout.activity_otpverification);
        phone=findViewById(R.id.phone);
        submit=findViewById(R.id.request_otp_button);
        hider =new ProgressBarHider(submit.getRootView(),submit);
        back=findViewById(R.id.back_button);
        countryCodeSpinner=findViewById(R.id.countrycode);
        adapter= new ArrayAdapter<String>(this,
                R.layout.spinner_layout, data.countryNames);
        adapter.setDropDownViewResource(R.layout.drop_down_tems);
        countryCodeSpinner.setAdapter(adapter);
        countrycode= data.countryAreaCodes;
        countryCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                code="+"+countrycode[i];
                //Toast.makeText(getApplicationContext(),code,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        context=this;
        sharedPref=getDefaultSharedPreferences(getApplicationContext());
        if(sharedPref.getString("TimeIndex","").equals("")){
            request_post_url=Generate_OTP;
        }
        else {
            request_post_url=Resend_OTP;
            flag=false;
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
            }
        });
    }

    public void verify() {


        if(phone.getText().toString().equals("")){
            String toastText =getString(R.string.toast_valid_ph_no);
            Toast.makeText(OTPVerification.this, toastText,Toast.LENGTH_LONG).show();
        }
        else {
            StringData=code+phone.getText().toString();
            hider.show();
            d = new DataModel();
            d.setPhoneNumber(StringData);
            if (!flag) {
                d.setTimeIndex(sharedPref.getString("TimeIndex", ""));
            }
            StringData = gson.toJson(d);
            Log.i("jisjoe", StringData);

            new HTTPAsyncTask2().execute(request_post_url);
        }


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(OTPVerification.this,ReceiverLogin.class));
        OTPVerification.this.finish();
        super.onBackPressed();
    }
private class HTTPAsyncTask2 extends AsyncTask<String, Void, String> {
    String response="Network Error";

    @Override
    protected String doInBackground(String... urls) {
        // params comes from the execute() call: params[0] is the url.
        try {
            try {
                response= HTTPPostGet.getJsonResponse(urls[0],StringData);
                Log.i("jisjoe",response.toString());
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return "Error!";
            }finally {
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
        hider.hide();
        JSONObject responseObject;
        try {
            responseObject = new JSONObject(response);
             Toast.makeText(getApplicationContext(),responseObject.getString("Message"),Toast.LENGTH_LONG).show();
             if(responseObject.getString("Message").equals("Success")) {
                 SharedPreferences.Editor editor = sharedPref.edit();
                 editor.putString("TimeIndex", responseObject.getString("TimeIndex"));
                 editor.putString("PhoneNumber", d.getPhoneNumber());
                 editor.apply();
                 startActivity(new Intent(getApplicationContext(), OTPValidation.class));
             }

        } catch (JSONException e) {
            e.printStackTrace();
        }    }


}}

