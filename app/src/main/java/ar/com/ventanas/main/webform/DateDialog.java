package ar.com.ventanas.main.webform;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by Marcelo on 24/02/2016.
 */
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
   EditText medtFechaInicioCurso, medtFechaFragBuscP1, medtFechaFragInactP1;

    public DateDialog() {
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + (monthOfYear+1) +  "/" + year;
        Bundle bundle=getArguments();
        String Pantalla=bundle.getString("Fragment");
        if (Pantalla.equals("FragmentBuscadores")) {
            medtFechaFragBuscP1 = (EditText) getActivity().findViewById(R.id.edtBuscEmplFechaUltExp);
            medtFechaFragBuscP1.setText(date);
        }
        else if (Pantalla.equals("FragmentInactivos")) {
            medtFechaFragInactP1 = (EditText) getActivity().findViewById(R.id.edtInactUltExp);
            medtFechaFragInactP1.setText(date);
        }
        else {
            medtFechaInicioCurso = (EditText) getActivity().findViewById(R.id.edtFechaInicioCurso);
            medtFechaInicioCurso.setText(date);
        }
    }

    public Dialog onCreateDialog (Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),this, year,month,day);

    }
}
