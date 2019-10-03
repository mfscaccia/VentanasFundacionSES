package ar.com.ventanas.main.webform;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by mscaccia on 24/02/2016.
 */
public class NotificationService extends Service {

    private final static String TAG = "CheckRecentPlay";
    private static Long MILLISECS_PER_DAY = 86400000L;
    private static Long MILLISECS_PER_MIN = 60000L;
    private static long delay3meses = 7776000000L;

    //  private static long delay = MILLISECS_PER_MIN * 1;   // 1 minutes (for testing)
    private static long delay = MILLISECS_PER_DAY * 2;   // 2 days

    private SharedPreferences proxNotificacion = null;
    public final static String PREFSPROXNOTIF = "PrefsFileProxNotif";
    private SharedPreferences.Editor editorproxNotificacion = null;

    SharedPreferences fechaInicioCurso ;
    SharedPreferences settings;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean Yacompletoencuesta() {
        DatabaseAdapter dba = new DatabaseAdapter();
        SQLiteDatabase database ;
        Integer completoencuesta = 0;
        String fechainiciocurso = "";
        try {
          database = dba.getDb();
          String selectQuery = "SELECT completoencuesta, fechaInicioCurso FROM datos";
          Cursor c = database.rawQuery(selectQuery,null);
          if (c.moveToFirst()) {
              completoencuesta = c.getInt(c.getColumnIndex("completoencuesta"));
              fechainiciocurso = c.getString(c.getColumnIndex("fechaInicioCurso"));
          }
          c.close();
          if (completoencuesta == 0) {
              return false;
          }
          else {
            return true;
          }
        }  catch(Exception e) {
            {
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.v(TAG, "Service started");
        settings = getSharedPreferences(MainActivity.PREFS, MODE_PRIVATE);
        fechaInicioCurso = getSharedPreferences(MainActivity.PREFSFECHA, MODE_PRIVATE);
        if (!Yacompletoencuesta()) {
            // Are notifications enabled?
           // if (settings.getBoolean("enabled", true)) {
                // Is it time for a notification?
         /*   long yourmilliseconds = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
            Date resultdate = new Date(yourmilliseconds);

            Log.v("CURRENTTIMEMILLISDELAY", sdf.format(resultdate) );*/

               // if (settings.getLong("lastRun", Long.MAX_VALUE) < System.currentTimeMillis()) {
                    // if ( System.currentTimeMillis()  >= settings.getLong("proxnotif", Long.MAX_VALUE)   )
            long timeInMilliseconds = 0;
            String givenDateString = fechaInicioCurso.getString("fechainiciocurso", "" );
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date mDate = sdf.parse(givenDateString.toString());
                timeInMilliseconds = mDate.getTime();
                Log.v("DATE IN MILLI",String.valueOf(timeInMilliseconds));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (System.currentTimeMillis() >= timeInMilliseconds * delay3meses)
              showNotification();
               // } else {
               //     Log.i(TAG, "Notifications are disabled");
             //   }

                // Set an alarm for the next time this service should run:
            setAlarm();

            Log.v(TAG, "Service stopped");
            stopSelf();

        } else {
            Log.v(TAG, "Service stopped definitivamente xq completo encuesta.");
            stopSelf();
        }
    }



    public void setAlarm() {
        long timeInMilliseconds = 0;
        String givenDateString = fechaInicioCurso.getString("fechainiciocurso", "" );
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date mDate = sdf.parse(givenDateString.toString());
            timeInMilliseconds = mDate.getTime();
            Log.v("DATE IN MILLI",String.valueOf(timeInMilliseconds));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.DAY_OF_MONTH, 10);

        Intent serviceIntent = new Intent(this, NotificationService.class);
        PendingIntent pi = PendingIntent.getService(this, 131313, serviceIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
       // am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pi);
        if (timeInMilliseconds != 0 ) {
            if (System.currentTimeMillis() >= timeInMilliseconds * delay3meses)
            {
                am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pi);
               // am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pi); //DE PRUEBA
                Log.v("DATE PRIMERA VEZ", String.valueOf(timeInMilliseconds + delay));

            }
            else {
                am.set(AlarmManager.RTC_WAKEUP, timeInMilliseconds + delay3meses, pi);
                Log.v("DATE PROXIMA VEZ", String.valueOf(System.currentTimeMillis() + delay3meses));

            }
          Log.v(TAG, "Alarm set");
        }
        else {
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pi);
            Log.v(TAG, "Alarm set 60000000");
        }
    }

    public void showNotification() {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker("Llegó el momento de contestar nuestra consulta!")
                .setSmallIcon(R.mipmap.ic_launcher )
                .setContentTitle("Programa Ventanas")
                .setContentText("Por favor, tomate 5 minutos para contestar nuestra consulta.")
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
