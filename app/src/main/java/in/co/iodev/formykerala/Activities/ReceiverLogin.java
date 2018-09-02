package in.co.iodev.formykerala.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;

import in.co.iodev.formykerala.Controllers.CheckInternet;
import in.co.iodev.formykerala.Controllers.HTTPPostGet;
import in.co.iodev.formykerala.Controllers.ProgressBarHider;
import in.co.iodev.formykerala.Models.DataModel;
import in.co.iodev.formykerala.Controllers.OTPTextEditor;
import in.co.iodev.formykerala.Models.data;
import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static in.co.iodev.formykerala.Constants.Constants.Receiver_Login;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class ReceiverLogin extends AppCompatActivity {
    EditText phone;
    Button submit;
    Gson gson = new Gson();
    SharedPreferences sharedPref;
    Boolean flag=true;
    DataModel d;
    EditText otp1,otp2,otp3,otp4;
    TextView forgot,register;
    Context context;
    ProgressBarHider hider;
    ImageView back;
    Spinner countryCodeSpinner;
    String countrycode[];
    String code;
    ArrayAdapter adapter;
    String StringData,StringData1,request_post_url=Receiver_Login,TimeIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }catch (Exception e){
            e.printStackTrace();
        }

        setContentView(R.layout.activity_reciever_login);
        MainActivity.setAppLocale(MainActivity.languagePreferences.getString("LOCALE_CODE", null), getResources());
        phone=findViewById(R.id.phone);
        otp1=findViewById(R.id.otp1);
        otp2=findViewById(R.id.otp2);
        otp3=findViewById(R.id.otp3);
        otp4=findViewById(R.id.otp4);
        forgot=findViewById(R.id.forg);
        otp1.addTextChangedListener(new OTPTextEditor(otp1,otp1.getRootView()));
        otp2.addTextChangedListener(new OTPTextEditor(otp2,otp2.getRootView()));
        otp3.addTextChangedListener(new OTPTextEditor(otp3,otp3.getRootView()));
        otp4.addTextChangedListener(new OTPTextEditor(otp4,otp4.getRootView()));
        sharedPref=getDefaultSharedPreferences(getApplicationContext());
        context=this;
        back=findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        submit=findViewById(R.id.request_otp_button);
        hider=new ProgressBarHider(submit.getRootView(),submit);
        sharedPref=getDefaultSharedPreferences(getApplicationContext());
        countryCodeSpinner=findViewById(R.id.countrycode);
        adapter= new ArrayAdapter<String>(this,
                R.layout.spinner_layout, data.countryNames);
        adapter.setDropDownViewResource(R.layout.drop_down_tems);
        countryCodeSpinner.setAdapter(adapter);
        countryCodeSpinner.setSelection(79);
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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
            }
        });
        register=findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReceiverLogin.this,OTPVerification.class));
                ReceiverLogin.this.finish();
            }
        });  forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReceiverLogin.this,ForgotPin.class));
                ReceiverLogin.this.finish();
            }
        });
    }

    public void verify() {


        StringData1=otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString();
        if(otp1.getText().toString().equals("")||otp2.getText().toString().equals("")||otp3.getText().toString().equals("")||otp4.getText().toString().equals("")||phone.getText().toString().equals("")){
            Toast.makeText(ReceiverLogin.this,"Please Enter Valid Phone and PIN",Toast.LENGTH_LONG).show();
        }
        else {
            StringData=code+phone.getText().toString();
            d = new DataModel();
            d.setPhoneNumber(StringData);
            d.setPIN(StringData1);
            StringData = gson.toJson(d);
            Log.i("jisjoe", StringData);

            hider.show();
            new HTTPAsyncTask2().execute(request_post_url);
        }


    }

    public void forgot(View view) {
       startActivity(new Intent(ReceiverLogin.this,ForgotPin.class));

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(ReceiverLogin.this,MainActivity.class));
        ReceiverLogin.this.finish();
        try {
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }catch (Exception e){
            e.printStackTrace();
        }
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
        JSONObject responseObject;
        try {
            hider.hide();
            responseObject = new JSONObject(response);
            Log.i("jisjoe",result);
             Toast.makeText(getApplicationContext(),responseObject.getString("Message"),Toast.LENGTH_LONG).show();
           if(responseObject.getString("Message").equals("Success")) {
               SharedPreferences.Editor editor = sharedPref.edit();
               String TimeIndex=responseObject.getString("TimeIndex");
               editor.putString("TimeIndex", responseObject.getString("TimeIndex"));
               editor.putString("PhoneNumber", d.getPhoneNumber());
               editor.putBoolean(TimeIndex+"Login", TRUE);

               editor.apply();
               TimeIndex=sharedPref.getString("TimeIndex","");
               if(responseObject.getString("Details_Entered").equals("True"))
               {             Log.d("jisjoe1",result);

                   editor.putBoolean(TimeIndex+"Edited", true);
                   if(responseObject.getString("Request_Added").equals("True"))
                   {   editor.putBoolean(TimeIndex+"FirstLogin",false);
                       editor.putBoolean(TimeIndex+"EditedR", true);
                   }
                   else
                   {editor.putBoolean(TimeIndex+"FirstLogin",true);
                       editor.putBoolean(TimeIndex+"EditedR", false);}
               }
               else
               {            Log.d("jisjoe2",result);

                   editor.putBoolean(TimeIndex+"Edited", false);
               }
               editor.apply();
               if(sharedPref.getBoolean(TimeIndex+"EditedR",FALSE)) {
                    startActivity(new Intent(getApplicationContext(), ReceiverRequirementsStatus.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)); //TO VIEW ADDED REQUESTS
                   ReceiverLogin.this.finish();
               }
               else
               if(sharedPref.getBoolean(TimeIndex+"Edited",FALSE)){
                   startActivity(new Intent(getApplicationContext(), ReceiverSelectRequirement.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)); //TO VIEW ADDED REQUESTS
                   ReceiverLogin.this.finish();
                     }
               else {
                   startActivity(new Intent(getApplicationContext(), ReceiverDetails.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)); //TO VIEW ADDED REQUESTS
                   ReceiverLogin.this.finish();


               }

           }

        } catch (JSONException e) {
            e.printStackTrace();
        }    }


}}

