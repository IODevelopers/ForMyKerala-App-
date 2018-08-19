package in.co.iodev.formykerala.Activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import in.co.iodev.formykerala.HTTPPost;
import in.co.iodev.formykerala.R;

public class OTPVerification extends AppCompatActivity {
    EditText phone;
    String StringData,request_post_url="",TimeIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
    }

    public void verify(View view) {
        phone=findViewById(R.id.phone);
        StringData=phone.getText().toString();
        new HTTPAsyncTask2().execute(request_post_url);



    }

private class HTTPAsyncTask2 extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
         String response;
        // params comes from the execute() call: params[0] is the url.
        try {
            try {
                response= HTTPPost.getJsonResponse(urls[0],null);
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

    }


}}

