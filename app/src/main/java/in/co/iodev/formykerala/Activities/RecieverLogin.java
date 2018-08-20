package in.co.iodev.formykerala.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import in.co.iodev.formykerala.HTTPPostGet;
import in.co.iodev.formykerala.Models.DataModel;
import in.co.iodev.formykerala.OTPTextEditor;
import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static in.co.iodev.formykerala.Constants.Constants.Generate_OTP;
import static in.co.iodev.formykerala.Constants.Constants.Resend_OTP;

public class RecieverLogin extends AppCompatActivity {
    EditText phone;
    Button submit,register;
    Gson gson = new Gson();
    SharedPreferences sharedPref;
    Boolean flag=true;
    DataModel d;
    EditText otp1,otp2,otp3,otp4;

    String StringData,StringData1,request_post_url=Reciever_Login,TimeIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciever_login);
        phone=findViewById(R.id.phone);
        otp1=findViewById(R.id.otp1);
        otp2=findViewById(R.id.otp2);
        otp3=findViewById(R.id.otp3);
        otp4=findViewById(R.id.otp4);
        otp1.addTextChangedListener(new OTPTextEditor(otp1,otp1.getRootView()));
        otp2.addTextChangedListener(new OTPTextEditor(otp2,otp2.getRootView()));
        otp3.addTextChangedListener(new OTPTextEditor(otp3,otp3.getRootView()));
        otp4.addTextChangedListener(new OTPTextEditor(otp4,otp4.getRootView()));

        submit=findViewById(R.id.request_otp_button);
        sharedPref=getDefaultSharedPreferences(getApplicationContext());
        if(sharedPref.getString("TimeIndex","").equals("")){
            request_post_url=Generate_OTP;
        }
        else {
            request_post_url=Resend_OTP;
            flag=false;
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
            }
        });
    }

    public void verify() {

        StringData=phone.getText().toString();
        StringData1=otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString();

        d=new DataModel();
        d.setPhoneNumber(StringData);
        d.setOTP(StringData1);
        StringData=gson.toJson(d);
        Log.i("jisjoe",StringData);

        new HTTPAsyncTask2().execute(request_post_url);



    }

private class HTTPAsyncTask2 extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
         String response;
        // params comes from the execute() call: params[0] is the url.
        try {
            try {
                response= HTTPPostGet.getJsonResponse(urls[0],StringData);
                Log.i("jisjoe",response.toString());
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return "Error!";
            }
        } catch (Exception e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        JSONObject response;
        JSONObject responseObject;
        try {
            responseObject = new JSONObject(result);
             Toast.makeText(getApplicationContext(),responseObject.getString("Message"),Toast.LENGTH_LONG).show();
           SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("TimeIndex", responseObject.getString("TimeIndex"));
            editor.putString("PhoneNumber", d.getPhoneNumber());
            editor.apply();
           // startActivity(new Intent(getApplicationContext(),OTPValidation.class)); //TO VIEW ADDED REQUESTS


        } catch (JSONException e) {
            e.printStackTrace();
        }    }


}}

