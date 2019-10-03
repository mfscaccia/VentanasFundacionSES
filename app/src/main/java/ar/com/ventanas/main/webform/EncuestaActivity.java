package ar.com.ventanas.main.webform;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mscaccia on 23/02/2016.
 */
public class EncuestaActivity  extends FragmentActivity {
    ViewPager vpEncuesta ;
    private DatabaseAdapter dba = new DatabaseAdapter();
    private SQLiteDatabase database ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encuesta_layout);

        Intent myIntent = getIntent(); // gets the previously created intent
        String NroDNI = myIntent.getStringExtra("NroDni");
        String FechaIngresada = myIntent.getStringExtra("FechaIngresada");

        vpEncuesta = (ViewPager) findViewById(R.id.viewpagerEncuesta);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        swipeAdapter.mDNI = NroDNI;
        swipeAdapter.mFechaIngresada = FechaIngresada;
        Date fechaIngresadaDate = ConvertToDate(FechaIngresada);

        Date fechaingmastresmeses;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaIngresadaDate); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, 90);
        fechaingmastresmeses = calendar.getTime();

        Date fechactual = new Date();


        vpEncuesta.setAdapter(swipeAdapter);
// SOLO PARA PRUEBA
        if (Yacompletoencuesta()){
            vpEncuesta.setCurrentItem(5);
        }
        else if (fechactual.before(fechaingmastresmeses) ) {
            vpEncuesta.setCurrentItem(4);
        }
        else {
            vpEncuesta.setCurrentItem(0);
        }
        //vpEncuesta.setCurrentItem(0); // linea comentada...se usa solo para prueba
    }



    private boolean Yacompletoencuesta() {
        Integer completoencuesta = 0;
        database = dba.getDb();
        String selectQuery = "SELECT completoencuesta FROM datos";
        Cursor c = database.rawQuery(selectQuery,null);
        if (c.moveToFirst()) {
            completoencuesta = c.getInt(c.getColumnIndex("completoencuesta"));
        }
        c.close();
        if (completoencuesta == 0) {
            return false;
        }
        else {
            return true;
        }


    }

//FIN SOLO PARA PRUEBA

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


}
