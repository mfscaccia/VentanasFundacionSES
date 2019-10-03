package ar.com.ventanas.main.webform;



import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
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
public class FragmentBuscadores extends Fragment {
    private EditText mEdtFechaUltExp, mEdtP2Otra, mEdtP3Otra;
    private RadioButton mRbP2Otra, mRbP3Otra;
    private RadioButton mRbP1Seleccionado, mRbP2Seleccionado, mRbP3Seleccionado;
    private Button mBtnEnvRtas;
    private RadioGroup mRgP1, mRgP2, mRgP3;
    private View mView;
    String mNroDni;
    Bundle mBundle ;



    public FragmentBuscadores() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_buscadores_layout, container, false);

        mEdtFechaUltExp = (EditText) mView.findViewById(R.id.edtBuscEmplFechaUltExp);
        mEdtFechaUltExp.setFocusable(false);
        mEdtFechaUltExp.setClickable(true);

        mEdtP2Otra = (EditText) mView.findViewById(R.id.edtBuscEmplP2Otra);
        mEdtP3Otra = (EditText) mView.findViewById(R.id.edtBuscEmplP3Otra);
        mRbP2Otra = (RadioButton) mView.findViewById(R.id.rbBuscEmplP2Otra);
        mRbP3Otra= (RadioButton) mView.findViewById(R.id.rbBuscEmplP3Otra);

        mRgP2 = (RadioGroup) mView.findViewById(R.id.radioGroupP2);
        mRgP3 = (RadioGroup) mView.findViewById(R.id.radioGroupP3);

        mBtnEnvRtas  = (Button) mView.findViewById(R.id.btnBuscadorestEnvRtas);

        mEdtP2Otra.setEnabled(false);
        mEdtP3Otra.setEnabled(false);

        mBundle = getArguments();
        mNroDni = mBundle.getString("DNI");



        mRgP2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                             @Override
                                             public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                 if (mRbP2Otra.isChecked()) {
                                                     mEdtP2Otra.setEnabled(true);
                                                     mEdtP2Otra.requestFocus();
                                                 }
                                                 else {
                                                     mEdtP2Otra.setText(""); ;
                                                     mEdtP2Otra.setEnabled(false);
                                                 }

                                             }
                                         }
        );

        mRgP3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                       @Override
                                                       public void onCheckedChanged(RadioGroup group, int checkedId) {

                                                           if (mRbP3Otra.isChecked()) {
                                                               mEdtP3Otra.setEnabled(true);
                                                               mEdtP3Otra.requestFocus();
                                                           }
                                                           else {
                                                               mEdtP3Otra.setText(""); ;
                                                               mEdtP3Otra.setEnabled(false);
                                                           }
                                                       }
                                                   }
        );

        mEdtFechaUltExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog = new DateDialog();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                Bundle b = new Bundle();
                b.putString("Fragment", "FragmentBuscadores");
                dialog.setArguments(b);
                dialog.show(ft, "DatePicker");
            }
        });


        mBtnEnvRtas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton P3otros = (RadioButton) mView.findViewById(R.id.rbBuscEmplP3Otra);
                RadioButton P2otros = (RadioButton) mView.findViewById(R.id.rbBuscEmplP2Otra);

                if (TextUtils.isEmpty(mEdtFechaUltExp.getText().toString().trim())) {
                    Toast.makeText(getActivity().getApplicationContext(), "Debe responder a todas las preguntas", Toast.LENGTH_LONG).show();

                }
                else if (mRgP2.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity().getApplicationContext(), "Debe responder a todas las preguntas.", Toast.LENGTH_LONG).show();
                }
                else if (P2otros.isChecked() && mEdtP2Otra.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Si selecciona 'Otra' como opción, complete el campo. ", Toast.LENGTH_LONG).show();
                }
                else if (mRgP3.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity().getApplicationContext(), "Debe responder a todas las preguntas.", Toast.LENGTH_LONG).show();
                }
                else if (P3otros.isChecked() && mEdtP3Otra.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Si selecciona 'Otra' como opción, complete el campo. ", Toast.LENGTH_LONG).show();
                }
                else {
                    PostDataTask postDataTask = new PostDataTask(getActivity().getApplicationContext(), getActivity());
                    // get selected radio button from radioGroup


                    int selectedId = mRgP2.getCheckedRadioButtonId();
                    mRbP2Seleccionado = (RadioButton) mView.findViewById(selectedId);
                    selectedId = mRgP3.getCheckedRadioButtonId();
                    mRbP3Seleccionado = (RadioButton) mView.findViewById(selectedId);


                    postDataTask.execute(Urls.postRespuestas,String.valueOf(2), mNroDni, getString(R.string.notrabajoperobusco),
                            mEdtFechaUltExp.getText().toString().trim(),
                            mRbP2Seleccionado.getText().toString().trim(),
                            mEdtP2Otra.getText().toString().trim(),
                            mRbP3Seleccionado.getText().toString().trim(),
                            mEdtP3Otra.getText().toString().trim()
                    );
                }

            }
        });


        return mView;


    }

}
