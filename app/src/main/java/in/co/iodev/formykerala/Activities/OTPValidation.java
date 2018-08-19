package in.co.iodev.formykerala.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.co.iodev.formykerala.R;

public class OTPValidation extends AppCompatActivity {
    SharedPreferences sharedPref;


    String StringData,request_post_url="https://e7i3xdj8he.execute-api.ap-south-1.amazonaws.com/Dev/otp/generate-otp",TimeIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpvalidation);

        TimeIndex=sharedPref.getString("TimeIndex","");


    }
}
