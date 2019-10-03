package ar.com.ventanas.main.webform;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by mscaccia on 23/02/2016.
 */
public class SwipeAdapter extends FragmentStatePagerAdapter {

    Fragment fragmentTrabajadores, fragmentBuscadores, fragmentInactivos,fragmentSituacion, fragmentFaltaPCompletar,
             fragmentGracias;
    public String mDNI;
    public String mFechaIngresada;
    private Bundle mBundle;

    public SwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                fragmentSituacion = new FragmentSituacion();
                mBundle = new Bundle();
                mBundle.putString("DNI", mDNI);
                fragmentSituacion.setArguments(mBundle);
              return fragmentSituacion;

            case 1:
                fragmentTrabajadores = new FragmentTrabajadores();
                mBundle = new Bundle();
                mBundle.putString("DNI", mDNI);
                fragmentTrabajadores.setArguments(mBundle);
                return fragmentTrabajadores;
            //case 2:
            case 2:
                fragmentBuscadores = new FragmentBuscadores();
                mBundle = new Bundle();
                mBundle.putString("DNI", mDNI);
                fragmentBuscadores.setArguments(mBundle);
                return fragmentBuscadores;
            case 3:
                fragmentInactivos = new FragmentInactivos();
                mBundle = new Bundle();
                mBundle.putString("DNI", mDNI);
                fragmentInactivos.setArguments(mBundle);
                return fragmentInactivos;
            case 4:
                fragmentFaltaPCompletar = new Fragmentfaltaparacompletar();
                mBundle = new Bundle();
                mBundle.putString("FechaIngresada", mFechaIngresada);
                fragmentFaltaPCompletar.setArguments(mBundle);
                return fragmentFaltaPCompletar;
            case 5: fragmentGracias = new fragmentGracias();
                    return fragmentGracias;
            default: {
                fragmentSituacion = new FragmentSituacion();
                mBundle = new Bundle();
                mBundle.putString("DNI", mDNI);
                fragmentSituacion.setArguments(mBundle);
                return fragmentSituacion;
                //fragmentTrabajadores = new FragmentTrabajadores();
                //return fragmentTrabajadores;
            }
        }

    }
    @Override
    public int getCount() {
        return 6;
    }
}
