package in.co.iodev.formykerala;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import in.co.iodev.formykerala.Activities.RecieverHome;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void receiver(View view) {
        startActivity(new Intent(MainActivity.this,RecieverHome.class));
    }
}
