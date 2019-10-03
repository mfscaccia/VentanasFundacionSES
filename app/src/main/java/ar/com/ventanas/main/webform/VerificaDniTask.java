package ar.com.ventanas.main.webform;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.webkit.CookieManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class VerificaDniTask extends AsyncTask<String, Void, String> {
    AsyncResult callback;
    private PowerManager.WakeLock mWakeLock;

    protected ProgressDialog progressDialog;
    public Context mContext;

    public VerificaDniTask(AsyncResult callback, Context ctx) {
        this.callback = callback;
        this.mContext = ctx;
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("Buscando su DNI en la base para acceder....aguarde");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Existe un error en la conexión o la misma es lenta, verifique que tenga acceso a internet y vuelva a intentar.";
        }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        // remove the unnecessary parts from the response and construct a JSON
        int start = result.indexOf("{", result.indexOf("{") + 1);
        int end = result.lastIndexOf("}");
        String jsonResponse = result.substring(start, end);
        try {
            JSONObject table = new JSONObject(jsonResponse);
            callback.onResult(table);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    private String downloadUrl(String urlString) throws IOException {
        InputStream is = null;

        try {
            System.setProperty("http.keepAlive", "false");
            CookieManager cm = null;
            URL url = new URL(urlString);
            int tries = 4;
            int code = 301;
            URLConnection conn = null;
            while (tries > 0 && code/100 == 3) {
                conn = null;
                conn = url.openConnection();
                //cm.setCookies(conn);
                ((HttpURLConnection)conn).setInstanceFollowRedirects(false);//Required
                code =((HttpURLConnection)conn).getResponseCode();
                if (code/100 == 3) {
                    String loc = conn.getHeaderField("Location");
                    url = new URL(loc);
                }
            }



            //HttpURLConnection conn = (HttpURLConnection) url.openConnection();




            //conn.setReadTimeout(10000 /* milliseconds */);
            //conn.setConnectTimeout(15000 /* milliseconds */);
            /* conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setInstanceFollowRedirects(true);
            // Starts the query
            conn.connect();
            int status = conn.getResponseCode();*/

            is = conn.getInputStream();

            String contentAsString = convertStreamToString(is);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
           }
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
