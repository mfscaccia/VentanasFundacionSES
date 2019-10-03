package ar.com.ventanas.main.webform;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mscaccia on 24/02/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "datos.db";
    private static final int DATABASE_VERSION = 1;
    private static Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE datos (id integer, fechaInicioCurso text, completoencuesta integer);");
        db.execSQL("insert into datos (id, fechaInicioCurso, completoencuesta) values (1,\"\",0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



}
