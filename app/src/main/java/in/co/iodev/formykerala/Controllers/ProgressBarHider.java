package in.co.iodev.formykerala.Controllers;

/**
 * Created by seby on 8/21/2018.
 */

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import in.co.iodev.formykerala.R;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by seby on 8/19/2018.
 */

public class ProgressBarHider
{
    private View Editview,view;
    private ProgressBar pbgar;
    public ProgressBarHider(View editview,View view)
    {
        this.Editview = editview;
        this.view=view;
        pbgar=editview.findViewById(R.id.pgbar);
    }
    public void show()
    {
        view.setVisibility(View.INVISIBLE);
        pbgar.setVisibility(View.VISIBLE);

    }
    public void hide()
    {
        pbgar.setVisibility(View.INVISIBLE);
        view.setVisibility(View.VISIBLE);

    }

}