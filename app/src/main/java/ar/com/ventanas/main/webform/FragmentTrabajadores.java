package ar.com.ventanas.main.webform;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class FragmentTrabajadores extends Fragment {

    private EditText mEdtP2Otra , mEdtP3Otra;
    private RadioButton mRbP2Otra, mRbP3Otra;
    private RadioButton mRbP1Seleccionado, mRbP2Seleccionado, mRbP3Seleccionado, mRbP4Seleccionado, mRbP5Seleccionado;
    private RadioGroup mRgP1, mRgP2, mRgP3, mRgP4, mRgP5;
    private Button mBtnEnvRtas;
    private View mView;
    String mNroDni;
    Bundle mBundle ;

    public FragmentTrabajadores() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_trabajadores_layout, container, false);

        mEdtP2Otra = (EditText) mView.findViewById(R.id.edtTrabP2Otra);
        mEdtP3Otra = (EditText) mView.findViewById(R.id.edtTrabP3Otra);
        mRbP2Otra = (RadioButton) mView.findViewById(R.id.rbTrabP2Otra);
        mRbP3Otra = (RadioButton) mView.findViewById(R.id.rbTrabP3Otra);

        mRgP1 = (RadioGroup) mView.findViewById(R.id.radioGroupP1);
        mRgP2 = (RadioGroup) mView.findViewById(R.id.radioGroupP2);
        mRgP3 = (RadioGroup) mView.findViewById(R.id.radioGroupP3);
        mRgP4 = (RadioGroup) mView.findViewById(R.id.radioGroupP4);
        mRgP5 = (RadioGroup) mView.findViewById(R.id.radioGroupP5);

        mBtnEnvRtas  = (Button) mView.findViewById(R.id.btnTrabajadoresEnvRtas);

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
                                                 } else {
                                                     mEdtP2Otra.setText("");

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
                                                 } else {
                                                     mEdtP3Otra.setText("");
                                                     mEdtP3Otra.setEnabled(false);
                                                 }
                                             }
                                         }
        );





        mBtnEnvRtas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton P2otros = (RadioButton) mView.findViewById(R.id.rbTrabP2Otra);
                RadioButton P3otros = (RadioButton) mView.findViewById(R.id.rbTrabP3Otra);
                /*if (TextUtils.isEmpty(mEdtFechaUltExp.getText().toString().trim())) {
                    Toast.makeText(getActivity().getApplicationContext(), "Debe responder a todas las preguntas", Toast.LENGTH_LONG).show();

                }
                else if (mRgP2.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity().getApplicationContext(), "Debe responder a todas las preguntas.", Toast.LENGTH_LONG).show();
                }*/

                if (mRgP1.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity().getApplicationContext(), "Debe responder a todas las preguntas.", Toast.LENGTH_LONG).show();
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
                else if (P3otros.isChecked() && mEdtP2Otra.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Si selecciona 'Otra' como opción, complete el campo. ", Toast.LENGTH_LONG).show();
                }
                else if (mRgP4.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity().getApplicationContext(), "Debe responder a todas las preguntas.", Toast.LENGTH_LONG).show();
                }
                else if (mRgP5.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity().getApplicationContext(), "Debe responder a todas las preguntas.", Toast.LENGTH_LONG).show();
                }


                else {
                    PostDataTask postDataTask = new PostDataTask(getActivity().getApplicationContext(), getActivity() );
                    // get selected radio button from radioGroup
                    int selectedId = mRgP1.getCheckedRadioButtonId();
                    mRbP1Seleccionado = (RadioButton) mView.findViewById(selectedId);
                    selectedId = mRgP2.getCheckedRadioButtonId();
                    mRbP2Seleccionado = (RadioButton) mView.findViewById(selectedId);
                    selectedId = mRgP3.getCheckedRadioButtonId();
                    mRbP3Seleccionado = (RadioButton) mView.findViewById(selectedId);
                    selectedId = mRgP4.getCheckedRadioButtonId();
                    mRbP4Seleccionado = (RadioButton) mView.findViewById(selectedId);
                    selectedId = mRgP5.getCheckedRadioButtonId();
                    mRbP5Seleccionado = (RadioButton) mView.findViewById(selectedId);


                    postDataTask.execute(Urls.postRespuestas,String.valueOf(1), mNroDni, getString(R.string.estoytrabajando),
                            mRbP1Seleccionado.getText().toString().trim(),
                            mRbP2Seleccionado.getText().toString().trim(),
                            mEdtP2Otra.getText().toString().trim(),
                            mRbP3Seleccionado.getText().toString().trim(),
                            mEdtP3Otra.getText().toString().trim(),
                            mRbP4Seleccionado.getText().toString().trim(),
                            mRbP5Seleccionado.getText().toString().trim()
                    );
                }

            }
        });

        return mView;
    }

}


