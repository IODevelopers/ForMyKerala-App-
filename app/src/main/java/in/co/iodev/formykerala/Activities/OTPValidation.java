package in.co.iodev.formykerala.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Trace;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.co.iodev.formykerala.HTTPPostGet;
import in.co.iodev.formykerala.Models.DataModel;
import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static java.lang.Boolean.TRUE;

public class OTPValidation extends AppCompatActivity {
    SharedPreferences sharedPref;
    EditText otp;
    Gson gson = new Gson();


    String StringData,request_post_url="https://e7i3xdj8he.execute-api.ap-south-1.amazonaws.com/Dev/otp/verify-otp",TimeIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpvalidation);
        sharedPref=getDefaultSharedPreferences(getApplicationContext());

        otp=findViewById(R.id.otp);
        TimeIndex=sharedPref.getString("TimeIndex","");


    }

    public void verify(View view) {
        StringData=otp.getText().toString();
        DataModel d=new DataModel();
        d.setOTP(StringData);
        d.setTimeIndex(TimeIndex);
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
            JSONObject responseObject= null;
            try {
                responseObject = new JSONObject(result);
                Toast.makeText(getApplicationContext(),responseObject.getString("Message"),Toast.LENGTH_LONG).show();
                if(responseObject.getString("Message").equals("Success")){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("Login",TRUE);
                    editor.apply();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }    }


    }}


