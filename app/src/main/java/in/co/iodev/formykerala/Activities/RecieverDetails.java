package in.co.iodev.formykerala.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import in.co.iodev.formykerala.HTTPPostGet;
import in.co.iodev.formykerala.Models.DataModel;
import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class RecieverDetails extends AppCompatActivity {
    EditText name,address,district,taluk;
    String Name,Address,District,Taluk;
    Gson gson = new Gson();
    String StringData;
    SharedPreferences sharedPref;
    String request_post_url="https://e7i3xdj8he.execute-api.ap-south-1.amazonaws.com/Dev/otp/generate-otp",TimeIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciever_details);
        name=findViewById(R.id.name);
        address=findViewById(R.id.address);
        district=findViewById(R.id.district);
        taluk=findViewById(R.id.taluk);
        sharedPref=getDefaultSharedPreferences(getApplicationContext());
        TimeIndex=sharedPref.getString("TimeIndex","");


    }

    public void next(View view) {
        Name=name.getText().toString();
        Address=address.getText().toString();
        District=district.getText().toString();
        Taluk=taluk.getText().toString();
        if(Name.equals("")||Address.equals("")||District.equals("")||Taluk.equals("")){
            Toast.makeText(getApplicationContext(),"Please provide all fields", Toast.LENGTH_LONG).show();
        }
        else {
            DataModel d=new DataModel();
            d.setName(Name);
            d.setAddress(Address);
            d.setDistrict(District);
            d.setTaluk(Taluk);
            d.setTimeIndex(TimeIndex);
            StringData=gson.toJson(d);
            new HTTPAsyncTask2().execute(request_post_url);

        }

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
             //   if(responseObject.getString("Message").equals("Success"))
           //     startActivity(new Intent(getApplicationContext(),OTPValidation.class));


            } catch (JSONException e) {
                e.printStackTrace();
            }    }


    }}


