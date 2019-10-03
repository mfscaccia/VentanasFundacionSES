package ar.com.ventanas.main.webform;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSituacion extends Fragment   {

    TextView mTexto;
    RadioButton mRadioTrabajando,mRadioBuscando, mRadioNoBusca;
    ViewPager vpEncuesta ;


    public FragmentSituacion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_situacion_layout, container,false);
        mRadioTrabajando = (RadioButton) view.findViewById(R.id.rbTrabajando);
        mRadioBuscando = (RadioButton) view.findViewById(R.id.rbBuscando);
        mRadioNoBusca = (RadioButton) view.findViewById(R.id.rbNobusca);
        vpEncuesta = (ViewPager) getActivity().findViewById(R.id.viewpagerEncuesta);


        mRadioTrabajando.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               vpEncuesta.setCurrentItem(1);         }
       });

        mRadioBuscando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpEncuesta.setCurrentItem(2);         }
        });

        mRadioNoBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpEncuesta.setCurrentItem(3);         }
        });


      /*  mTexto = (TextView) view.findViewById(R.id.textview1);
        //return inflater.inflate(R.layout.fragment_situacion_layout, container, false);
        Bundle bundle = getArguments();
        String message = Integer.toString(bundle.getInt("count"));
        mTexto.setText(message);*/
        return view;
    }



}


