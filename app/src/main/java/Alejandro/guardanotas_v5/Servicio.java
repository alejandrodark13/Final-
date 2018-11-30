package Alejandro.guardanotas_v5;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Alejandro.guardanotas_v5.Datos.DaoNotas;
import Alejandro.guardanotas_v5.Datos.DaoNotifica;
import Alejandro.guardanotas_v5.Datos.POJO_Alerta_Serial;
import Alejandro.guardanotas_v5.Datos.POJO_Nota_Serial;
import jorgemedina.guardanotas_v5.R;


public class Servicio extends Service {

    @Override
    public void onCreate(){}

    hilo h;
    @Override
    public int onStartCommand(Intent intent,int flag,int idProcess){
        try{
            if(h==null) {
                h = new hilo();
                h.start();
            }
        }catch (Exception err){}
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        try{
            if(h.isAlive()) {
                h.stop();
            }
        }catch (Exception err){}
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private ArrayList<POJO_Alerta_Serial> listarecordatorios = new ArrayList<>() ;
    int aux=0;
    public void btnNoti_click(String Titulo,String Descripcion,int indice) {
        aux=aux+1;
        NotificationCompat.Builder mBuilder;
        NotificationManager mNotifyMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        int icono = R.mipmap.ic_launcher;
        long hora = System.currentTimeMillis();
        Intent i = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
        mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                .setContentIntent(pendingIntent)
                .setSmallIcon(icono)
                .setContentTitle(Titulo)
                .setContentText(Descripcion)
                .setWhen(hora)
                .setVibrate(new long[]{100, 250, 100, 500})
                .setAutoCancel(true)
                .setSound(defaultSound);
        mNotifyMgr.notify(aux, mBuilder.build());
    }


    public class hilo extends Thread{
        @Override
        public  void  run(){
            while (true){
                try {
                    DaoNotifica dao = new DaoNotifica(getApplicationContext());
                    DaoNotas daonotas = new DaoNotas(getApplicationContext());
                    final Calendar c= Calendar.getInstance();
                    String fecha = c.get(Calendar.DAY_OF_MONTH)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.YEAR);
                    String hora = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
                    List<POJO_Alerta_Serial> lista ;
                    lista = dao.buscarTodosDeFecha(fecha);
                    POJO_Alerta_Serial alertas = new POJO_Alerta_Serial();
                    alertas.setId_alerta(1);
                    alertas.setId_tarea(1);
                    alertas.setTituloAlerta("Titulo");
                    alertas.setDescripcionAlerta("Descripcion");
                    alertas.setFechaAlerta("Fecha");
                    alertas.setHoraAlerta("Hora");
                    lista.add(alertas);
                    for (int i = 0; i < lista.size(); i++) {
                        if (lista.get(i).getHoraAlerta().equalsIgnoreCase(hora) && ((c.get(Calendar.SECOND)) == 0)) {
                            POJO_Alerta_Serial obj = new POJO_Alerta_Serial();
                            obj.setId_alerta(lista.get(i).getId_alerta());
                            obj.setFechaAlerta(lista.get(i).getFechaAlerta());
                            obj.setHoraAlerta(lista.get(i).getHoraAlerta());
                            listarecordatorios.add(obj);
                            POJO_Nota_Serial nota = new POJO_Nota_Serial();
                            nota = daonotas.buscarUno(lista.get(i).getId_tarea());
                            if(nota.getTitulo().trim().length()>0 && nota.getDescripcion().trim().length()>0) {
                                btnNoti_click(nota.getTitulo(), nota.getDescripcion(), i);
                            }

                        }

                    }
                    Thread.sleep(1000);
                } catch (Exception e) {}
            }
        }
    }
}
