package in.co.iodev.formykerala.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.co.iodev.formykerala.R;

public class Help_view extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setAppLocale(MainActivity.languagePreferences.getString("LOCALE_CODE", null), getResources());
        setContentView(R.layout.activity_help);
    }
}
