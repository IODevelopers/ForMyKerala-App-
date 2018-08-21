package in.co.iodev.formykerala.Controllers;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by seby on 8/19/2018.
 */

public class HTTPPost
        {
            public static String getJsonResponse(String myUrl, String StringData) throws IOException
            {
                String result = "";

                URL url = new URL(myUrl);

                // 1. create HttpURLConnection Error!
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(StringData);
                writer.flush();
                writer.close();
                os.close();

                // 4. make POST request to the given URL
                conn.connect();
                Log.d("Responsefrom1st",conn.getResponseMessage().toString());

                // 5. return response message
                return conn.getResponseMessage()+"";
            }


        }