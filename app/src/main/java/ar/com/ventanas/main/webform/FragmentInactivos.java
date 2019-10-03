package ar.com.ventanas.main.webform;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentInactivos extends Fragment {
    private EditText mEdtFechaUltExp, mEdtP2Otra;
    private RadioButton mRbP2Otra, mRbP2Seleccionado;

    private Button mBtnEnvRtas;
    private RadioGroup mRgP2;
    private View mView;
    String mNroDni;
    Bundle mBundle ;

    /*CONTROLES DEL FORM*/

    //URL derived from form URL
    public static final String URL="https://docs.google.com/forms/d/1dcq0Fgt5dSUoIiH119K5-eF-XMGiq3eVPt97Aocspx4/formResponse";
    //input element ids found from the live form page


    public FragmentInactivos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_inactivos_layout, container, false);

        mEdtFechaUltExp = (EditText) mView.findViewById(R.id.edtInactUltExp);
        mEdtFechaUltExp.setFocusable(false);
        mEdtFechaUltExp.setClickable(true);

        mEdtP2Otra = (EditText) mView.findViewById(R.id.edtInacP2Otra);
        mRbP2Otra = (RadioButton) mView.findViewById(R.id.rbInactP2Otra);
        mBtnEnvRtas  = (Button) mView.findViewById(R.id.btnInactEnvRtas);

        mRgP2 = (RadioGroup) mView.findViewById(R.id.radioGroupP2);

        mEdtP2Otra.setEnabled(false);

        mBundle = getArguments();
        mNroDni = mBundle.getString("DNI");

        mEdtFechaUltExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog = new DateDialog();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                Bundle b = new Bundle();
                b.putString("Fragment", "FragmentInactivos");
                dialog.setArguments(b);
                dialog.show(ft, "DatePicker");
            }
        });

        mRgP2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                             @Override
                                             public void onCheckedChanged(RadioGroup group, int checkedId) {

                                                 if (mRbP2Otra.isChecked()) {
                                                     mEdtP2Otra.setEnabled(true);
                                                     mEdtP2Otra.requestFocus();
                                                 } else {
                                                     mEdtP2Otra.setText("");

                                                     mEdtP2Otra.setEnabled(false);
                                                 }
                                             }
                                         }
        );




        mBtnEnvRtas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             RadioButton P2otros = (RadioButton) mView.findViewById(R.id.rbInactP2Otra);

             if (TextUtils.isEmpty(mEdtFechaUltExp.getText().toString().trim())) {
                 Toast.makeText(getActivity().getApplicationContext(), "Debe responder a todas las preguntas", Toast.LENGTH_LONG).show();

             }
             else if (mRgP2.getCheckedRadioButtonId() == -1) {
                     Toast.makeText(getActivity().getApplicationContext(), "Debe responder a todas las preguntas.", Toast.LENGTH_LONG).show();
             }
             else if (P2otros.isChecked() && mEdtP2Otra.getText().toString().trim().equalsIgnoreCase("")) {
                 Toast.makeText(getActivity().getApplicationContext(), "Si selecciona 'Otra' como opción, complete el campo. ", Toast.LENGTH_LONG).show();
             }

             else {
                 PostDataTask postDataTask = new PostDataTask(getActivity().getApplicationContext(), getActivity());
                 // get selected radio button from radioGroup
                 int selectedId = mRgP2.getCheckedRadioButtonId();

               //  String fecha[] = mEdtFechaUltExp.getText().toString().trim().split("/");
                 //String strMes =

                 // find the radiobutton by returned id
                 mRbP2Seleccionado = (RadioButton) mView.findViewById(selectedId);

                 postDataTask.execute(Urls.postRespuestas,String.valueOf(3), mNroDni, getString(R.string.notrabnobusco),
                                      mEdtFechaUltExp.getText().toString().trim(),
                                      mRbP2Seleccionado.getText().toString().trim(),
                                      mEdtP2Otra.getText().toString().trim() );

             }

            }
        });

        return mView;
    }

   /* private String obtenerTextoMes(int mes){
        switch (mes) {
            case 1:
                return ""
                break;

        return "";
    }*/

}
