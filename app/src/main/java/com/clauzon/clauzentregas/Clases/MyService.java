package com.clauzon.clauzentregas.Clases;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.clauzon.clauzentregas.NavDrawerActivity;
import com.clauzon.clauzentregas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

public class MyService extends Service {

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    MyTask mi_tarea;

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseON();
        mi_tarea = new MyTask();
    }

    public void firebaseON() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mi_tarea.execute();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        mi_tarea.cancel(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public class MyTask extends AsyncTask<String, String, String> {

        private final static String CHANEL_ID = "NOTIFICATION";
        private Boolean aux;
        private PendingIntent pendingIntent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            aux = true;
        }

        private void createNotificationChanel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Notificacion";
                NotificationChannel notificationChannel = new NotificationChannel(CHANEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        private void createNotificaicon(String concepto) {
            Intent intent = new Intent(getApplicationContext(), NavDrawerActivity.class);
            pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,intent,  PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANEL_ID);
            builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
            builder.setContentTitle(concepto);
            builder.setContentText("Toca para abrir aplicación");
            builder.setColor(Color.RED);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            builder.setLights(Color.MAGENTA, 1000, 1000);
            builder.setVibrate(new long[]{500, 500, 500});
            builder.setDefaults(Notification.DEFAULT_SOUND);

            builder.setContentIntent(pendingIntent);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(0, builder.build());
        }

        private void ShowNotification() {
            DatabaseReference reference = database.getReference();
            reference.child("Notificaciones").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Notificacion notificacion = dataSnapshot.getValue(Notificacion.class);
                    if (notificacion.getId_usuaio().equals(currentUser.getUid())) {


                        createNotificationChanel();
                        createNotificaicon(notificacion.getConcepto());
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        @Override
        protected void onProgressUpdate(String... values) {
            //super.onProgressUpdate(values);
            ShowNotification();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            aux = false;
        }

        @Override
        protected String doInBackground(String... strings) {
            while (aux) {
                try {
                    publishProgress();
                    Thread.sleep(3600000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }

    }
}
