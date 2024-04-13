package com.example.permisoscelular;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import android.net.NetworkInfo;

public class MainActivity extends AppCompatActivity {

    //1. Declaracion de objetos de la interfaz que se usarán en la parte lógica
    private Button btnCheckPermissions;
    private Button btnGuardarArchivo;
    private TextView tvResponse;

    //1.1 Objetos para recursos
    private TextView versionAndroid;
    private EditText etNombreArchivo;
    private int versionSDK;
    private ProgressBar pbLevelBatt;
    private TextView tvLevelBatt;

    private int nivelBateria;
    private TextView tvConexion;
    IntentFilter batFilter;
    CameraManager cameraM;
    String cameraId;
    private Button btnOn;
    private Button btnOff;
    ConnectivityManager conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LinearLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //3. Llamado del método encargado de enlazar objetos
        initObject();
        //Internet
        obtenerConexion();

        //4. Enlace de botones a sus correspondientes métodos
        btnCheckPermissions.setOnClickListener(this::voidCheckPermissions);
        btnGuardarArchivo.setOnClickListener(this::guardarArchivo);

        //Botones para la linterna
        btnOn.setOnClickListener(this::onLight);
        btnOff.setOnClickListener(this::offLigth);

        //Bateria
        batFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, batFilter);
    }

    //2. Enlace de objetos
    private void initObject(){
        btnCheckPermissions = findViewById(R.id.btnCheckPermission);
        btnGuardarArchivo = findViewById(R.id.btnGuardarArchivo);
        tvResponse = findViewById(R.id.tvResponse);
        etNombreArchivo = findViewById(R.id.etNombreArchivo);
        versionAndroid = findViewById(R.id.tvVersionAndroid);
        pbLevelBatt = findViewById(R.id.pbLevelBaterry);
        tvLevelBatt = findViewById(R.id.tvLevelBaterry);
        tvConexion = findViewById(R.id.tvConexion);
        btnOn = findViewById(R.id.btnOn);
        btnOff = findViewById(R.id.btnOff);
    }

    //5. Verificación de permisos
    private void voidCheckPermissions(View view){
        //Si hay permiso --> 0, sino --> -1
        int statusCamera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        int statusAudio = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        int statusCell = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);
        int statusSMS = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);
        int statusLocation = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);

        Intent datos = new Intent(this, VistaPermisos.class);
        datos.putExtra("camera", statusCamera);
        datos.putExtra("audio", statusAudio);
        datos.putExtra("cell", statusCell);
        datos.putExtra("sms", statusSMS);
        datos.putExtra("location", statusLocation);
        startActivity(datos);
    }

    private void guardarArchivo(View view){
        File ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String nombreArchivo = etNombreArchivo.getText() + ".txt";

        try {
            FileOutputStream escribirArchivo = new FileOutputStream(new File(ruta, nombreArchivo));
            ObjectOutputStream streamArchivo = new ObjectOutputStream(escribirArchivo);

            streamArchivo.writeObject("\r\nVersion Android: " + Build.VERSION.RELEASE + "/ SDK: " + Build.VERSION.SDK_INT + "\r\n" +
                    "Nivel de Bateria Celular " + nivelBateria + "%");
            streamArchivo.close();
            tvResponse.setText("Archivo " + nombreArchivo + " creado exitosamente");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    //8. Implementación del onResume para la version de Android
    @Override
    protected void onResume() {
        super.onResume();
        String versionSO = Build.VERSION.RELEASE;
        versionSDK = Build.VERSION.SDK_INT;
        versionAndroid.setText("Version SO: " + versionSO + " /SDK: " + versionSDK);
    }

    //9. Encedido y apagado de camara
    private void onLight(View view){
        try{
            cameraM = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
            cameraId = cameraM.getCameraIdList()[0];
            cameraM.setTorchMode(cameraId, true);
            Toast.makeText(this, "Linterna Encendida", Toast.LENGTH_SHORT).show();
        }
        catch(Exception ex){
            Toast.makeText(this, "No se puede encender la linterna", Toast.LENGTH_SHORT).show();
            Log.i("FLASH", ex.getMessage());
        }
    }

    private void offLigth(View view){
        try{
            cameraM.setTorchMode(cameraId, false);
            Toast.makeText(this, "Linterna Apagada", Toast.LENGTH_SHORT).show();
        }
        catch(Exception ex){
            Toast.makeText(this, "No se puede apagar la linterna", Toast.LENGTH_SHORT).show();
            Log.i("FLASH", ex.getMessage());
        }
    }

    //10. Bateria
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int levelBattery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            nivelBateria = levelBattery;
            pbLevelBatt.setProgress(levelBattery);
            tvLevelBatt.setText("Level Baterry " + levelBattery + "%");
        }
    };

    //11. Internet
    private void obtenerConexion(){
        conexion = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo redInfo = conexion.getActiveNetworkInfo();

        if(redInfo != null && redInfo.isConnected()){
            tvConexion.setText("The device is connected to the Internet");
        }
        else{
            tvConexion.setText("The device is not connected to the Internet.");
        }
    }
}