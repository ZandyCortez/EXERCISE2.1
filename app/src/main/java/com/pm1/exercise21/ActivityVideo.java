package com.pm1.exercise21;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.pm1.exercise21.configuracion.SQLiteConexion;
import com.pm1.exercise21.configuracion.Transacciones;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityVideo extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE=104;
    VideoView vview;
    Button btnvideo;
    Button btnsalvar;
    String currentvidpath;
    byte[] bArrayVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        vview = (VideoView) findViewById(R.id.videoView);
        btnvideo = (Button) findViewById(R.id.btnvideo);
        btnsalvar = (Button) findViewById(R.id.btnsalva);
        btnsalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bArrayVideo != null){
                    guardaVideo();
                }else{
                    if(bArrayVideo == null)
                    {
                        Toast.makeText(getApplicationContext(),"Necesitar Grabar un Video primero. para Guardar!", Toast.LENGTH_LONG).show();
                        vview.requestFocus();}
                }
            }
        });
        btnvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 permisos();
            }
        });

//        String pathh = Environment.getExternalStorageDirectory()+"";
//        vview.setVideoPath("/sdcard/DCIM/Camera/00b412ba4d2d0b5a06fcb0dbd8f93a9b.mp4");
//          vview.setVideoPath("storage/emulated/0/ddmsrec.mp4");
//            vview.setMediaController(new MediaController(this));//mostrar los controles de video
//            vview.requestFocus();//poner el focus en el video
//            vview.start();//iniciar el video de un solo
    }
    private void permisos() {//pedir los permisos
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},REQUEST_VIDEO_CAPTURE);
        }
        else{
            dispatchTakeVideoIntent();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_VIDEO_CAPTURE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                tomarvideo();//si el permiso de video existe tomar el video
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Se necesesita el permiso de la CAMARA",Toast.LENGTH_LONG).show();
        }
    }
    private void tomarvideo() {
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (videoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(videoIntent, REQUEST_VIDEO_CAPTURE);//capturar el video
        }
    }

    private void guardaVideo() {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDataBase,null,1);//nueva conexion a a DB
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.video, bArrayVideo);
        String sql = "INSERT INTO tblvideo(id,video) VALUES (0,'"+bArrayVideo+"')";
        try{
//            db.execSQL(sql);
            Long resultado = db.insert(Transacciones.tablaVideo,Transacciones.id,valores);
            Toast.makeText(getApplicationContext(),"VIDEO Ingresado! COD: "+resultado.toString(), Toast.LENGTH_LONG).show();

            db.close();
        }catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"error "+e, Toast.LENGTH_LONG).show();
        }
        limpiarPantalla();
        bArrayVideo=null;
    }

    private void limpiarPantalla() {
        vview.setVideoURI(null);
    }
    private File createVideoFile() throws IOException {
	        // Crear el archivo
	        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	        String videoFileName = "MP4_" + timeStamp + "_";
	        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
	        File vid = File.createTempFile(
	                videoFileName,  /* prefix */
	                ".mp4",         /* suffix */
	                storageDir      /* directory */
	        );

	        // Save a file: path for use with ACTION_VIEW intents
	        currentvidpath = vid.getAbsolutePath();
	        return vid;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            File vidFile = new File(currentvidpath);
            Uri urivideo = Uri.fromFile(vidFile);
            vview.setVideoURI(urivideo);//establecer el video a el videoview desde la uri
            vview.setMediaController(new MediaController(this));//mostrar los controles de video
            vview.requestFocus();//poner el focus en el video
            vview.start();//iniciar el video de un solo
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    bArrayVideo = Files.readAllBytes(vidFile.toPath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void dispatchTakeVideoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);//crear el intent de la foto
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File videofile = null;
            try {
                videofile = createVideoFile();//crear una nueva variable de archivo
                Toast.makeText(getApplicationContext(),"Archivo creado",Toast.LENGTH_LONG).show();
                AlertDialog.Builder adb=new AlertDialog.Builder(ActivityVideo.this);
                  adb.setTitle("EXITO");
                  adb.setMessage("Archivo creado");
                  adb.setPositiveButton("Ok", null);
                  adb.show();
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(),"Error al guardar",Toast.LENGTH_LONG).show();
            }
            SystemClock.sleep(1000);//tiempo de espera
            Uri vidURI = null;
            if (videofile != null) {
                try {
                    vidURI = FileProvider.getUriForFile(this,"com.pm1.exercise21.provider",videofile);//crear una uri apartir de el path del archivo guardado
                    Toast.makeText(getApplicationContext(),"Ruta Obtenida",Toast.LENGTH_LONG).show();
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(),"Error de ruta "+ex,Toast.LENGTH_LONG).show();
                    System.out.println(ex);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, vidURI);//enviarle al intent el path
                startActivityForResult(takePictureIntent, REQUEST_VIDEO_CAPTURE);//iniciar a tomar la foto
            }
        }
    }
}