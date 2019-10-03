package ar.com.ventanas.main.webform;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mscaccia on 01/03/2016.
 */
public class Fragmentfaltaparacompletar extends Fragment {

    private EditText mEdtFaltanXDias;
    private String mFechaIngresadaStr;

    public Fragmentfaltaparacompletar() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faltaparacompletar_layout, container, false);
        mEdtFaltanXDias = (EditText) view.findViewById(R.id.edtFaltanXDias);
        Bundle bundle = getArguments();
        mFechaIngresadaStr = bundle.getString("FechaIngresada");
        Date mFechaingresadaDate;
        mFechaingresadaDate = ConvertToDate(mFechaIngresadaStr);
        SimpleDateFormat dfDate  = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date d = null;
        java.util.Date d1 = null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(mFechaingresadaDate);
        cal.add(Calendar.DATE,90);

        try {
            d = new Date();// dfDate.parse();

            d1 = dfDate.parse(dfDate.format(cal.getTime()));//Returns 15/10/2012
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        int diffInDays = (int) (( d1.getTime() - d.getTime() )/ (1000 * 60 * 60 * 24));

        mEdtFaltanXDias.setText("Faltan " + diffInDays + " dias para que puedas completar la encuesta." );
        mEdtFaltanXDias.setEnabled(true);
        mEdtFaltanXDias.setFocusable(false);
        return view;
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



}
