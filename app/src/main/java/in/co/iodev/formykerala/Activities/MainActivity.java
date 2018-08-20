package in.co.iodev.formykerala.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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

        if(sharedPref.getBoolean("Login",FALSE
        ))
        {
            if(sharedPref.getBoolean("Edited",FALSE))
                startActivity(new Intent(getApplicationContext(),ReceiverRequirementsStatus.class)); //TO VIEW ADDED REQUESTS
            else
                startActivity(new Intent(getApplicationContext(),ReceiverSelectRequirement.class)); //TO VIEW ADDED REQUESTS


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
    }
    public void donor() {
/*
        startActivity(new Intent(MainActivity.this,ReceiverRoleSelectActivity.class));
*/
    }
}