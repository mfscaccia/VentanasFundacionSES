package ar.com.ventanas.main.webform;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;




public class MainActivity extends AppCompatActivity {
        private final static String TAG1 = "Acerca de";
        private Context mContext;
        private EditText documentEditText, medtFecha;
        private TextView txtInicial;
         private String mURL = "";
        private ArrayList<Alumno> mAlumnos = new ArrayList<Alumno>();
        private Button mSendButton, mBtnFecha ;
        private DatabaseAdapter dba = new DatabaseAdapter();
        private SQLiteDatabase database ;
        private final static String TAG = "MainActivity";
        public final static String PREFS = "PrefsFile";
        public final static String PREFSFECHA = "PrefsFileFecha";


        private SharedPreferences settings = null;
        private SharedPreferences fechaForService = null;


        private SharedPreferences.Editor editor = null;
        private SharedPreferences.Editor editorfecha = null;

        private String mFechaInicioCurso;

        private static final int NOTIFY_ME_ID=1337;

    @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            // Save time of run:
            settings = getSharedPreferences(PREFS, MODE_PRIVATE);
            fechaForService = getSharedPreferences(PREFSFECHA, MODE_PRIVATE);
            editor = settings.edit();
            editorfecha = fechaForService.edit();
            txtInicial = (TextView) findViewById(R.id.textViewInicial);

            txtInicial.setMovementMethod(new ScrollingMovementMethod());


            //save the activity in a context variable to be used afterwards
            mContext =this;
            dba.abrir(mContext);

            mFechaInicioCurso = ConsultarFechaInicioCurso();
            if (!mFechaInicioCurso.equalsIgnoreCase("")){
              editorfecha.putString("fechainiciocurso", mFechaInicioCurso );
              editorfecha.commit();
              // First time running app?
              if (!settings.contains("lastRun"))
                  enableNotification(null);
              else
                  recordRunTime();
              Log.v(TAG, "Starting CheckRecentRun service...");
            }
               //startService(new Intent(this, NotificationService.class));

            //Get references to UI elements in the layout
            mSendButton = (Button)findViewById(R.id.sendButton);
            //mBtnFecha = (Button) findViewById(R.id.btnFechaInicio);
            documentEditText = (EditText)findViewById(R.id.edtNroDocumento);
            medtFecha = (EditText) findViewById(R.id.edtFechaInicioCurso);
// SOLO PARA PRUEBAS
              medtFecha.setFocusable(false);
              medtFecha.setClickable(true);
            if (!TextUtils.isEmpty(mFechaInicioCurso)){
                medtFecha.setEnabled(false);
                medtFecha.setText(mFechaInicioCurso);
            }
            else {
                medtFecha.setEnabled(true);
                medtFecha.setFocusable(false);
                medtFecha.setClickable(true);
            }
// FIN SOLO PRUEBAS

            medtFecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateDialog dialog = new DateDialog();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Bundle b = new Bundle();
                    b.putString("Fragment","Main");
                    dialog.setArguments(b);
                    dialog.show(ft, "DatePicker");
                }
            });


            mSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Make sure all the fields are filled with values
                    Date fechaInicio;
                    fechaInicio = ConvertToDate(medtFecha.getText().toString().trim());
                    if (TextUtils.isEmpty(documentEditText.getText().toString().trim())) {
                        Toast.makeText(mContext, "Debe ingresar el número de documento.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else if (TextUtils.isEmpty(medtFecha.getText().toString().trim())) {
                        Toast.makeText(mContext, "Debe ingresar la fecha de inicio del curso", Toast.LENGTH_LONG).show();
                        return;
                    }
                   else if ((medtFecha.isEnabled()) && (fechaMayoraActual(fechaInicio))) {
                        Toast.makeText(mContext, "No puede ingresar una fecha mayor a la fecha actual. Verifique la fecha ingresada o la de su dispositivo", Toast.LENGTH_LONG).show();
                        return;
                   }
                    // DE PRUEBA
                    else if ((medtFecha.isEnabled()) && (fechaMayora2meses(fechaInicio))) {
                        Toast.makeText(mContext, "No puede ingresar una fecha anterior a los 2 meses. Verifique la fecha ingresada o la de su dispositivo", Toast.LENGTH_LONG).show();
                        return;
                    }
                    // FIN DE PRUEBA
                    else if (!isOnline()) {
                        Toast.makeText(mContext, "Existen problemas en su conexión a internet. Verifique que tenga acceso a internet y vuelva a intentar", Toast.LENGTH_LONG).show();
                        return;
                    }
                    mURL = Urls.selectBDDocumentos;

                    if (!mURL.isEmpty()) {
                        new VerificaDniTask(new AsyncResult() {
                            @Override
                            public void onResult(JSONObject object) {
                                processJson(object);
                            }
                        },mContext).execute(mURL);

                    }
                }
            });
        }

    @Override
    protected void onResume() {
        super.onResume();
//        startService(new Intent(mContext, NotificationService.class));
        if (!ConsultarFechaInicioCurso().equalsIgnoreCase("")) {
            medtFecha.setEnabled(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.acercade:
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                AcercaDeFragment fragAcercade = new AcercaDeFragment();
                fragAcercade.show(fm,TAG1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Date ConvertToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }


    private boolean fechaMayoraActual(Date fecha){
        Date fechaactual = new Date();
        if (fecha.getTime() > fechaactual.getTime()) {
          return true;
        }
        else {
            return false;
        }
    }

    private boolean fechaMayora2meses(Date fecha){
        Date actualmenos2;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date()); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, -60);
        actualmenos2 = calendar.getTime();

        if (fecha.before(actualmenos2)){
            return true;
        }
        else {
            return false;
        }



    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public void recordRunTime() {
        editor.putLong("lastRun", System.currentTimeMillis());
        editor.commit();
    }

    public void enableNotification(View v) {
        editor.putLong("lastRun", System.currentTimeMillis());
        editor.putBoolean("enabled", true);
        editor.commit();
        Log.v(TAG, "Notifications enabled");
    }

    public void disableNotification(View v) {
        editor.putBoolean("enabled", false);
        editor.commit();
        Log.v(TAG, "Notifications disabled");
    }

    private String ConsultarFechaInicioCurso() {
        String fechadeInicioCargada = "";
        database = dba.getDb();
        String selectQuery = "SELECT fechaInicioCurso FROM datos";
        Cursor c = database.rawQuery(selectQuery,null);
        if (c.moveToFirst()) {
            fechadeInicioCargada = c.getString(c.getColumnIndex("fechaInicioCurso"));
        }
        c.close();

        return fechadeInicioCargada;
    }

    private void processJson(JSONObject object) {

        try {
            JSONArray rows = object.getJSONArray("rows");
            boolean encontro = false;
            int r = 0 ;
            while (!encontro && r < rows.length() ) {

              JSONObject row = rows.getJSONObject(r);
              JSONArray columns = row.getJSONArray("c");

              int dni = columns.getJSONObject(2).getInt("v");

              if (documentEditText.getText().toString().equals(String.valueOf(dni)) ) {
                  // llamar al activity
                  encontro = true;
                  Toast.makeText(mContext,"DNI ENCONTRADO!!! WUHU!",Toast.LENGTH_LONG).show();
                  Intent intent = new Intent(this,EncuestaActivity.class);
                  database = dba.getDb();
                  database.beginTransaction();
                  try {
                      ContentValues values = new ContentValues();
                      values.put("fechaInicioCurso", medtFecha.getText().toString());
                      database.update("datos", values, null, null);
                      database.setTransactionSuccessful();

                  } finally {
                      database.endTransaction();
                  }

                  fechaForService = getSharedPreferences(PREFSFECHA, MODE_PRIVATE);
                  editorfecha = fechaForService.edit();
                  mFechaInicioCurso = ConsultarFechaInicioCurso();
                  editorfecha.putString("fechainiciocurso", mFechaInicioCurso );
                  editorfecha.commit();
                  startService(new Intent(this, NotificationService.class));
                  intent.putExtra("NroDni", documentEditText.getText().toString().trim());
                  intent.putExtra("FechaIngresada", medtFecha.getText().toString().trim());

                  startActivity(intent);
              }
              r++;


            }
            if (!encontro) {
                    // MENSAJE DNI ERRONEO INEXISTENTE.
                    Toast.makeText(mContext,"No encontramos tu DNI en nuestros datos. Pedile ayuda a tu tutor para solucionarlo",Toast.LENGTH_LONG).show();

            }
            /* for (int r = 0; r < rows.length(); ++r) {

                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");

                int dni = columns.getJSONObject(2).getInt("v");
                /*int month = 0;//columns.getJSONObject(1).getInt("v");
                int day = 0;//columns.getJSONObject(2).getInt("v");
                String homeTeam = columns.getJSONObject(0).getString("v");
                int homeScore = 0;//columns.getJSONObject(4).getInt("v");
                String awayTeam = columns.getJSONObject(1).getString("v");
                int awayScore = 0;//columns.getJSONObject(6).getInt("v");*/
                // Match m = new Match(year, month, day, homeTeam, homeScore, awayTeam, awayScore);

               /* if (documentEditText.getText().toString().equals(String.valueOf(dni)) ) {
                    // llamar al activity
                    Toast.makeText(mContext,"LLAMAR ACTIVITY",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this,EncuestaActivity.class);
                    startActivity(intent);

                } else {
                    // MENSAJE DNI ERRONEO INEXISTENTE.
                    Toast.makeText(mContext,"DNI ERRONEO",Toast.LENGTH_LONG).show();
                }
                //Alumno a = new Alumno(dni);
                //mAlumnos.add(a);

                //matches.add(m);
            } */

            /*final MatchesAdapter adapter = new MatchesAdapter(this, R.layout.match, matches);
            matchesListView.setAdapter(adapter);

            this.infoBox.setText(this.message);*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

        //AsyncTask to send data as a http POST request
    /*    private class VerificaDNITask extends AsyncTask<String, Void, Boolean> {

            @Override
            protected Boolean doInBackground(String... contactData) {
                Boolean result = true;
                String dni   = contactData[0];
                /*String email = contactData[1];
                String subject = contactData[2];
                String message = contactData[3];
                String postBody=""; */

               /*try {
                    //all values must be URL encoded to make sure that special characters like & | ",etc.
                    //do not cause problems
                    postBody = NOMBRE_KEY+"=" + URLEncoder.encode(email,"UTF-8") +
                            "&" + APELLIDO_KEY + "=" + URLEncoder.encode(subject,"UTF-8") +
                            "&" + MESSAGE_KEY + "=" + URLEncoder.encode(message,"UTF-8");
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


                try{
                    //Create OkHttpClient for sending request
                    OkHttpClient client = new OkHttpClient();
                    //Create the request body with the help of Media Type
                    RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
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
                Toast.makeText(mContext,result?"Message successfully sent!":"There was some error in sending message. Please try again after some time.",Toast.LENGTH_LONG).show();
            }

        }*/


