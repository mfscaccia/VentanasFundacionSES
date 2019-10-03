package ar.com.ventanas.main.webform;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mscaccia on 26/02/2016.
 */
public class PostDataTask extends AsyncTask<String, Void, Boolean> {

    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private Context mContext;
    private Activity mMainActivity;
    private SQLiteDatabase database ;

    public PostDataTask(Context mContext, Activity mainActivity) {
        this.mContext = mContext;
        this.mMainActivity = mainActivity;
    }

    @Override
        protected Boolean doInBackground(String... contactData) {
            Boolean result = true;
            String url = contactData[0];
            String form = contactData[1];
            String dni = contactData[2];
            String situacion = contactData[3];

//            String message = contactData[3];
            String postBody="";

            try { // trabajadores
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems // HAY Q MANDARLE TODAS LAS OTRAS KEYS VACIAS.
                if (form.equals("1")) {
                    String rta1 = contactData[4];
                    String rta2 = contactData[5];
                    String rta2otros = contactData[6];
                    String rta3 = contactData[7];
                    String rta3otros = contactData[8];
                    String rta4 = contactData[9];
                    String rta5 = "";//especial para el 1- al 5.
                    String[] rta5array = contactData[10].split("-");
                    rta5 = rta5array[0];


                    if (!TextUtils.isEmpty(rta2otros)) {
                        rta2 = "__other_option__";
                    }

                    if (!TextUtils.isEmpty(rta3otros)) {
                        rta3 = "__other_option__";
                    }

                    postBody =
                            Keys.NRODOCUMENTO_KEY + "=" + URLEncoder.encode(dni, "UTF-8") +
                                    "&" + Keys.SITUACION_KEY    + "=" + URLEncoder.encode(situacion, "UTF-8") +
                                    "&" + Keys.TRABAJADORES_P1 + "=" + URLEncoder.encode(rta1, "UTF-8") +
                                    "&" + Keys.TRABAJADORES_P2 + "=" + URLEncoder.encode(rta2, "UTF-8") +
                                    "&" + Keys.TRABAJADORES_P2_OTROS + "=" + URLEncoder.encode(rta2otros, "UTF-8") +
                                    "&" + Keys.TRABAJADORES_P3 + "=" + URLEncoder.encode(rta3, "UTF-8") +
                                    "&" + Keys.TRABAJADORES_P3_OTROS + "=" + URLEncoder.encode(rta3otros, "UTF-8") +
                                    "&" + Keys.TRABAJADORES_P4 + "=" + URLEncoder.encode(rta4, "UTF-8") +
                                    "&" + Keys.TRABAJADORES_P5 + "=" + URLEncoder.encode(rta5.trim(), "UTF-8") +
                                    "&" +   "pageHistory=0,1";
                                    // VER LA 2 Y LA 3 QUE TIENEN OTROS ahi cambiaria el key.
                }

                if (form.equals("2")) { // buscadores
                    String fecha[] = contactData[4].split("/");
                    String rta2 = contactData[5];
                    String rta2otros = contactData[6];
                    String rta3 = contactData[7];
                    String rta3otros = contactData[8];

                    if (!TextUtils.isEmpty(rta2otros)) {
                        rta2 = "__other_option__";
                    }

                    if (!TextUtils.isEmpty(rta3otros)) {
                        rta3 = "__other_option__";
                    }

                    postBody =
                            Keys.NRODOCUMENTO_KEY    + "=" + URLEncoder.encode(dni, "UTF-8") +
                      "&" + Keys.SITUACION_KEY       + "=" + URLEncoder.encode(situacion, "UTF-8") +
                      "&" + Keys.BUSCADORES_P1_MES   + "=" + URLEncoder.encode(fecha[1], "UTF-8") +
                      "&" + Keys.BUSCADORES_P1_DIA   + "=" + URLEncoder.encode(fecha[0], "UTF-8") +
                      "&" + Keys.BUSCADORES_P1_ANIO  + "=" + URLEncoder.encode(fecha[2], "UTF-8") +
                      "&" + Keys.BUSCADORES_P2       + "=" + URLEncoder.encode(rta2, "UTF-8") +
                      "&" + Keys.BUSCADORES_P2_OTROS + "=" + URLEncoder.encode(rta2otros, "UTF-8") +
                      "&" + Keys.BUSCADORES_P3       + "=" + URLEncoder.encode(rta3, "UTF-8") +
                      "&" + Keys.BUSCADORES_P3_OTROS + "=" + URLEncoder.encode(rta3otros, "UTF-8") +
                      "&" +   "pageHistory=0,2";


                }


                if (form.equals("3")) { // inactivos
                    String fecha[] = contactData[4].split("/");
                    String rta2  = contactData[5];
                    String rta2otros  = contactData[6];

                    if (!TextUtils.isEmpty(rta2otros)) {
                        rta2 = "__other_option__";
                    }

                    postBody =
                                          Keys.NRODOCUMENTO_KEY + "=" + URLEncoder.encode(dni, "UTF-8") +
                                    "&" + Keys.SITUACION_KEY + "=" + URLEncoder.encode(situacion, "UTF-8") +
                                    "&" + Keys.INACTIVOS_P1MES + "=" + URLEncoder.encode(fecha[1], "UTF-8") +
                                    "&" + Keys.INACTIVOS_P1DIA + "=" + URLEncoder.encode(fecha[0], "UTF-8") +
                                    "&" + Keys.INACTIVOS_P1ANIO + "=" + URLEncoder.encode(fecha[2], "UTF-8") +
                                    "&" + Keys.INACTIVOS_P2 + "=" + URLEncoder.encode(rta2, "UTF-8") +
                                    "&" + Keys.INACTIVOS_P2_OTROS + "=" + URLEncoder.encode(rta2otros, "UTF-8") +
                                    "&" +   "pageHistory=0,3";
                }
            } catch (UnsupportedEncodingException ex) {
                result=false;
            }



            /*
            //If you want to use HttpRequest class from http://stackoverflow.com/a/2253280/1261816
            try {
			HttpRequest httpRequest = new HttpRequest();
			httpRequest.sendPost(url, postBody);
		}catch (Exception exception){
			result = false;
		}
            */

            try{
                //Create OkHttpClient for sending request
                OkHttpClient client = new OkHttpClient();
                //Create the request body with the help of Media Type
                RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                        request.toString();
                //Send the request
                Response response = client.newCall(request).execute();


            }catch (IOException exception){
                result=false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            //Print Success or failure message accordingly
            Toast.makeText(mContext, result ? "Respuesta enviada correctamente!" : "Ups! Hubo un error al enviar la respuesta, verifique su conexión e intente nuevamente.", Toast.LENGTH_LONG).show();
            if (result) {
                ViewPager vpEncuesta;
                vpEncuesta = (ViewPager) mMainActivity.findViewById(R.id.viewpagerEncuesta);
                vpEncuesta.setCurrentItem(5);

                try {

                    DatabaseAdapter dba = new DatabaseAdapter();
                    database = dba.getDb();
                    database.beginTransaction();
                    ContentValues values = new ContentValues();
                    values.put("completoencuesta", 1);
                    database.update("datos", values, null, null);
                    database.setTransactionSuccessful();

                } finally {
                    database.endTransaction();
                }
            }
        }

    }

