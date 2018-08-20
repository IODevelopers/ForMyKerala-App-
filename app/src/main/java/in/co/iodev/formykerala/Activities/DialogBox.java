package in.co.iodev.formykerala.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import in.co.iodev.formykerala.R;

/**
 * Created by seby on 8/21/2018.
 */

public class DialogBox  extends Dialog {

        public String name;
        public Boolean status;
        public TextView Name;
        Button accept,decline;
        public Activity activity;
        public DialogBox(Activity activity,String name) {
            super(activity);
            this.name=name;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialogcard);
            Name=findViewById(R.id.receiver_namw);
            accept=findViewById(R.id.accept);
            decline=findViewById(R.id.decline);
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    status=true;
                }
            });
            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    status=false;
                }
            });
            Name.setText(name);

        }
       Boolean getStatus()
       {
           return status;
       }
    }

