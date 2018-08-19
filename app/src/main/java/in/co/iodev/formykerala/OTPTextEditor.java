package in.co.iodev.formykerala;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

/**
 * Created by seby on 8/19/2018.
 */

public class OTPTextEditor implements TextWatcher
{
    private View Editview,view;
    public OTPTextEditor(View editview,View view)
    {
        this.Editview = editview;
        this.view=view;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // TODO Auto-generated method stub
        String text = editable.toString();
        switch(Editview.getId())
        {

            case R.id.otp1:
                if(text.length()==1)
                    view.findViewById(R.id.otp2).requestFocus();
                if(text.length()<1)
                    view.findViewById(R.id.otp1).requestFocus();

                break;
            case R.id.otp2:
                if(text.length()==1)
                    view.findViewById(R.id.otp3).requestFocus();
                if(text.length()<1)
                    view.findViewById(R.id.otp1).requestFocus();
                break;
            case R.id.otp3:
                if(text.length()==1)
                    view.findViewById(R.id.otp4).requestFocus();
                if(text.length()<1)
                    view.findViewById(R.id.otp2).requestFocus();
                break;
            case R.id.otp4:
                if(text.length()<1)
                    view.findViewById(R.id.otp3).requestFocus();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }
}