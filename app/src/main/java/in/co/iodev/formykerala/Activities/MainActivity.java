package in.co.iodev.formykerala.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.sql.Time;

import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static java.lang.Boolean.FALSE;

public class MainActivity extends AppCompatActivity {
Button receiver,donor;
SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref=getDefaultSharedPreferences(getApplicationContext());
        String TimeIndex=sharedPref.getString("TimeIndex","");
        if(sharedPref.getBoolean(TimeIndex+"Login",FALSE))
        {
            if(sharedPref.getBoolean(TimeIndex+"EditedR",FALSE)) {
                startActivity(new Intent(getApplicationContext(), ReceiverRequirementsStatus.class)); //TO VIEW ADDED REQUESTS
                MainActivity.this.finish();
            }
            else
                if(sharedPref.getBoolean(TimeIndex+"Edited",FALSE)){
                    startActivity(new Intent(getApplicationContext(),ReceiverSelectRequirement.class)); //TO SELECT ITEMS
                    MainActivity.this.finish();
                }
                else {
                    startActivity(new Intent(getApplicationContext(), ReceiverDetails.class)); //TO ADD NEW REQUEST
                    MainActivity.this.finish();
                }

        }
        else if(sharedPref.getBoolean(TimeIndex+"DLogin",FALSE))
        {
            if(sharedPref.getBoolean(TimeIndex+"DEditedR",FALSE)) {
                startActivity(new Intent(getApplicationContext(), DonorHomeActivity.class)); //TO VIEW ADDED REQUESTS
                MainActivity.this.finish();
            }
            else
            if(sharedPref.getBoolean(TimeIndex+"DEdited",FALSE)){
                startActivity(new Intent(getApplicationContext(),DonorSelectItems.class)); //TO SELECT ITEMS
                MainActivity.this.finish();
            }
            else{
                startActivity(new Intent(getApplicationContext(),DonorDetails.class)); //TO ADD NEW REQUEST
                MainActivity.this.finish();
                }

        }
        receiver=findViewById(R.id.role_receiver);
        donor=findViewById(R.id.role_Donor);
        receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiver();
            }
        });
        donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donor();
            }
        });
    }

    public void receiver() {
        startActivity(new Intent(MainActivity.this,ReceiverLogin.class));
        MainActivity.this.finish();
    }
    public void donor() {
        startActivity(new Intent(MainActivity.this,DonorLogin.class));
        MainActivity.this.finish();
    }
}
