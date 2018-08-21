package in.co.iodev.formykerala.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import in.co.iodev.formykerala.Controllers.CheckInternet;
import in.co.iodev.formykerala.Controllers.HTTPPostGet;
import in.co.iodev.formykerala.Controllers.ProgressBarHider;
import in.co.iodev.formykerala.Models.DataModel;
import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static in.co.iodev.formykerala.Constants.Constants.DForgot_PIN_Generate;
import static in.co.iodev.formykerala.Constants.Constants.Generate_OTP_Forget;

public class DForgotPin extends AppCompatActivity {
    EditText phone;
    Button submit;
    Gson gson = new Gson();
    SharedPreferences sharedPref;
    Boolean flag=true;
    ImageView back;
    DataModel d;
    Context context;
    ProgressBarHider hider;



    String StringData,request_post_url=DForgot_PIN_Generate,request_post_url1=Generate_OTP_Forget,TimeIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        phone=findViewById(R.id.phone);
        back=findViewById(R.id.back_button);
        submit=findViewById(R.id.request_otp_button);
        sharedPref=getDefaultSharedPreferences(getApplicationContext());
        context=this;
        hider=new ProgressBarHider(submit.getRootView(),submit);


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
        hider.show();

        StringData=phone.getText().toString();
        if(StringData.equals(""))
        {
            Toast.makeText(DForgotPin.this,"Please enter a Valid Phone number",Toast.LENGTH_LONG).show();
        }
        else {
            d = new DataModel();
            d.setPhoneNumber(StringData);
            StringData = gson.toJson(d);
            Log.i("jisjoe", StringData);

            new HTTPAsyncTask2().execute(request_post_url);

        }

    }

    private class HTTPAsyncTask2 extends AsyncTask<String, Void, String> {
        String response;

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
                } finally {
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
                if(responseObject.getString("Message").equals("Success")){
                    new HTTPAsyncTask3().execute(request_post_url1);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }    }


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DForgotPin.this,DonorLogin.class));
        DForgotPin.this.finish();
        super.onBackPressed();
    }

    private class HTTPAsyncTask3 extends AsyncTask<String, Void, String> {

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
        @Override
        protected void onPreExecute() {
            CheckInternet CI=new CheckInternet();
            CI.isOnline(context);
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
                startActivity(new Intent(getApplicationContext(),DForgotPinOTPValidation.class));


            } catch (JSONException e) {
                e.printStackTrace();
            }    }


    }}

