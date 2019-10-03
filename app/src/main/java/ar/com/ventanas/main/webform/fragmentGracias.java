package ar.com.ventanas.main.webform;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentGracias extends Fragment {

    EditText mEdtGracias;

    public fragmentGracias() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gracias, container, false);
        mEdtGracias = (EditText) view.findViewById(R.id.edtGracias);

        mEdtGracias.setText("Muchas gracias por tu participación.\n" +
                "Equipo Proyecto Ventanas");
        mEdtGracias.setEnabled(true);
        mEdtGracias.setFocusable(false);

        return view;
    }

}
