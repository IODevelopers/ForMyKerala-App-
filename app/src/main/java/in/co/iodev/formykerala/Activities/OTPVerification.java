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

        // params comes from the execute() call: params[0] is the url.
        try {
            try {
                return HttpPost2(urls[0]);
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

    private String HttpPost2(String myUrl) throws IOException {
        String result = "";

        URL url = new URL(myUrl);

        // 1. create HttpURLConnection Error!
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(StringData);
        writer.flush();
        writer.close();
        os.close();

        // 4. make POST request to the given URL

        conn.connect();

        Log.d("Response from second", conn.getResponseMessage().toString());
        int responseCode = conn.getResponseCode();
        Log.d("Response Code:", String.valueOf(responseCode));
        if (responseCode == HttpsURLConnection.HTTP_OK) {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            conn.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            while ((line = in.readLine()) != null) {

                sb.append(line);
                break;
            }
            in.close();
            return conn.getResponseMessage() + "";

        } else {
            return null;
        }
        // 5. return response message

    }
}}

