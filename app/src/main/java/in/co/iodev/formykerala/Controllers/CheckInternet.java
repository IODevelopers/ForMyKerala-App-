package in.co.iodev.formykerala.Controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CheckInternet {
    private static CheckInternet instance = new CheckInternet();
//    static Context context;
    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;

//    public static CheckInternet getInstance(Context ctx) {
//        context = ctx.getApplicationContext();
//        return instance;
//    }
    public void isOnline(Context context) {
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            Log.d("CheckInternet",String.valueOf(connected));
            if(!connected){
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        //set icon
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        //set title
                        .setTitle("Internet Issues")
                        //set message
                        .setMessage("Please check if internet is available")
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //set what would happen when Neutral button is clicked
                            }
                        }).show();


            }


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
    }
}
