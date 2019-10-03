package ar.com.ventanas.main.webform;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by mscaccia on 24/02/2016.
 */
public class DatabaseAdapter {

    private static DatabaseHelper mDatabaseHelper = null;
    private static SQLiteDatabase mDb = null;

    public void abrir(Context context) {
        this.cerrar();
        mDatabaseHelper = new DatabaseHelper(context);
        // TODO: si la conexion ya estaba abierta generar un except, seguro falto cerrarActualizacion
        mDb = mDatabaseHelper.getWritableDatabase();
    }

    public void cerrar () {
        if (this.isOpen()) {
            mDb.close();
            mDb = null; //En Java "tradicional" esto ayudaba a liberar mas rapido la memoria, no se en android
        }
        if (mDatabaseHelper != null) {
            mDatabaseHelper.close();
            mDatabaseHelper = null;
        }
    }

    public static SQLiteDatabase getDb () {
        // TODO si la base no esta abierta, ni las condiciones dadas, generar un exception
        // TODO: Crear un exception de "falta inicializar"
        if (mDatabaseHelper==null) {
            return null;
        }

        if ((mDb == null)||(!mDb.isOpen())) {
            mDb = mDatabaseHelper.getWritableDatabase();
        }

        return mDb;
    }

    public boolean isOpen () {
        //Dejo el nombre en ingles por practicidad
        // TODO : renombrar por estaAbierto ?
        return ((mDb != null)&&(mDb.isOpen()));

    }
}
