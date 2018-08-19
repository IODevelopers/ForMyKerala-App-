package in.co.iodev.formykerala.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static java.lang.Boolean.FALSE;

public class RecieverHome extends AppCompatActivity {
SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciever_home);
        sharedPref=getDefaultSharedPreferences(getApplicationContext());
        if(sharedPref.getBoolean("Login",FALSE))
        {
            Toast.makeText(getApplicationContext(),"LOGGED IN ALREADY--REDIRECT",Toast.LENGTH_LONG).show();
        }
    }

    public void request(View view) {

    }

    public void view_items(View view) {
    }
}
