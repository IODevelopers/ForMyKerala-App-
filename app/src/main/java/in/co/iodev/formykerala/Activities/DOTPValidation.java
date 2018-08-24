package in.co.iodev.formykerala.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import in.co.iodev.formykerala.Controllers.CheckInternet;
import in.co.iodev.formykerala.Controllers.HTTPPostGet;
import in.co.iodev.formykerala.Controllers.ProgressBarHider;
import in.co.iodev.formykerala.Models.DataModel;
import in.co.iodev.formykerala.Controllers.OTPTextEditor;
import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static in.co.iodev.formykerala.Constants.Constants.Resend_OTP;
import static in.co.iodev.formykerala.Constants.Constants.Verify_OTP;

public class DOTPValidation extends AppCompatActivity {
    SharedPreferences sharedPref;
    EditText otp1,otp2,otp3,otp4;
    Button verify,resend_otp;
    Gson gson = new Gson();
    CardView otp_resend;
    ImageView back;
    Context context;
    ProgressBarHider hider;
    int minutes=2,seconds=0;
    long delay=120000;
    String StringData,request_post_url=Verify_OTP,TimeIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpvalidation);
        sharedPref=getDefaultSharedPreferences(getApplicationContext());
        otp1=findViewById(R.id.otp1);
        otp2=findViewById(R.id.otp2);
        otp3=findViewById(R.id.otp3);
        otp4=findViewById(R.id.otp4);
        verify=findViewById(R.id.otp_verify);
        context=this;
        otp1.addTextChangedListener(new OTPTextEditor(otp1,otp1.getRootView()));
        otp2.addTextChangedListener(new OTPTextEditor(otp2,otp2.getRootView()));
        otp3.addTextChangedListener(new OTPTextEditor(otp3,otp3.getRootView()));
        otp4.addTextChangedListener(new OTPTextEditor(otp4,otp4.getRootView()));
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
            }
        });
        otp_resend=findViewById(R.id.resend_otp);
        resend_otp=findViewById(R.id.otp_resend);
        back=findViewById(R.id.back_button);
        TextView phone=findViewById(R.id.phone);
        phone.setText("to "+sharedPref.getString("PhoneNumber",""));
        hider=new ProgressBarHider(verify.getRootView(),verify);
        timer();
        new Timer().schedule(new resendotp(),delay);
        TimeIndex=sharedPref.getString("TimeIndex","");
        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataModel d=new DataModel();
                d.setTimeIndex(TimeIndex);
                d.setPhoneNumber(sharedPref.getString("PhoneNumber",""));
                StringData=gson.toJson(d);
                new HTTPAsyncTask3().execute(Resend_OTP);


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }
    public void timer()
    {    final TextView textTimer=findViewById(R.id.timer);
        new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                textTimer.setText("Resend OTP in "+minutes+":"+checkDigit(seconds));

                if(seconds==0)
                {
                    seconds=59;
                    minutes--;
                }
                else {
                    seconds--;
                }



            }

            public void onFinish() {
                minutes=2;
                seconds=0;
                textTimer.setText("try again");
            }

        }.start();




    }
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);}


    private class resendotp extends TimerTask
    {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    otp_resend.setVisibility(View.VISIBLE);

                }
            });
        }
    }

    public void verify() {
        if(otp1.getText().toString().equals("")||otp2.getText().toString().equals("")||otp3.getText().toString().equals("")||otp4.getText().toString().equals("")){
            Toast.makeText(DOTPValidation.this,"Please Enter Valid OTP",Toast.LENGTH_LONG).show();
        }
        else {
            hider.show();
            StringData = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString();
            DataModel d = new DataModel();
            d.setOTP(StringData);
            d.setTimeIndex(TimeIndex);
            StringData = gson.toJson(d);
            Log.i("jisjoe", StringData);

            new HTTPAsyncTask2().execute(Verify_OTP);
        }
    }
    boolean doubleBackToExitPressedOnce = false;


    @Override
    public void onBackPressed() {
        startActivity(new Intent(DOTPValidation.this,DonorLogin.class));
        DOTPValidation.this.finish();
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
            JSONObject responseObject= null;
            hider.hide();
            try {
                responseObject = new JSONObject(response);
                Toast.makeText(getApplicationContext(),responseObject.getString("Message"),Toast.LENGTH_LONG).show();
                if(responseObject.getString("Message").equals("Success"))
                {
                    Intent intent = new Intent(DOTPValidation.this,DPinSelection.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Wrong OTP ",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
    private class HTTPAsyncTask3 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response="Network Error";
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
            JSONObject responseObject= null;
            try {
                responseObject = new JSONObject(result);
                otp_resend.setVisibility(View.INVISIBLE);
                timer();
                new Timer().schedule(new resendotp(),delay);
                Toast.makeText(getApplicationContext(),responseObject.getString("Message"),Toast.LENGTH_LONG).show();


            } catch (JSONException e) {
                e.printStackTrace();
            }    }


    }}


