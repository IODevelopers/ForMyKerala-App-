package in.co.iodev.formykerala.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import in.co.iodev.formykerala.R;

public class ReceiverRoleSelectActivity extends AppCompatActivity {
    Button register_request,view_requests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_role_select);
        register_request=findViewById(R.id.receiver_register_request);
        register_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReceiverRoleSelectActivity.this,OTPVerification.class));
            }
        });
    }
}
