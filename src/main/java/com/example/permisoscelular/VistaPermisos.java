package com.example.permisoscelular;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.annotation.NonNull;

public class VistaPermisos extends AppCompatActivity {

    private TextView tvCamera;
    private TextView tvAudio;
    private TextView tvCell;
    private TextView tvSendSMS;
    private TextView tvLocation;
    private TextView tvResponse;
    private Button btnRequestPermissionsCamera;
    private Button btnRequestPermissionsAudio;
    private Button btnRequestPermissionsCell;
    private Button btnRequestPermissionsSMS;
    private Button getBtnRequestPermissionsLocation;
    private Button btnVolver;
    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_AUDIO = 2;
    public static final int REQUEST_CODE_CELL = 3;
    public static final int REQUEST_CODE_SENDSMS = 4;
    public static final int REQUEST_CODE_LOCATION = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vista_permisos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initObjects();
        mostrarPermisos();
        btnRequestPermissionsCamera.setOnClickListener(this::voidRequestPermissionsCamera);
        btnRequestPermissionsAudio.setOnClickListener(this::voidRequestPermissionsAudio);
        btnRequestPermissionsCell.setOnClickListener(this::voidRequestPermissionsCell);
        btnRequestPermissionsSMS.setOnClickListener(this::voidRequestPermissionsSMS);
        getBtnRequestPermissionsLocation.setOnClickListener(this::voidRequestPermissionsLocation);
        btnVolver.setOnClickListener(this::regresar);
    }

    private void initObjects(){
        tvCamera = findViewById(R.id.tvCamera);
        tvAudio = findViewById(R.id.tvAudio);
        tvCell = findViewById(R.id.tvCell);
        tvResponse = findViewById(R.id.tvResponse);
        tvSendSMS = findViewById(R.id.tvSendSMS);
        tvLocation = findViewById(R.id.tvLocation);
        btnRequestPermissionsCamera = findViewById(R.id.btnRequestPermissionCamera);
        btnRequestPermissionsAudio = findViewById(R.id.btnResquestPermissionAudio);
        btnRequestPermissionsCell = findViewById(R.id.btnRequestPermissionCell);
        btnRequestPermissionsSMS = findViewById(R.id.btnRequestPermissionSMS);
        getBtnRequestPermissionsLocation = findViewById(R.id.btnRequestPermissionLocation);
        btnVolver = findViewById(R.id.btnVolver);
    }

    //Método que imprime el estado de los permisos del activity principal a este
    private void mostrarPermisos(){
        Bundle recogerDatos = getIntent().getExtras();
        tvCamera.setText("Status Camera:" + recogerDatos.getInt("camera"));
        tvAudio.setText("Status Audio:" + recogerDatos.getInt("audio"));
        tvCell.setText("Status Call Phone:" + recogerDatos.getInt("cell"));
        tvSendSMS.setText("Status SMS:" + recogerDatos.getInt("sms"));
        tvLocation.setText("Status Location:" + recogerDatos.getInt("location"));
    }

    //Método que actualiza los permisos al aceptarlos
    private void actualizarPermisos(){
        tvCamera.setText("Status Camera:" + ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA));
        tvAudio.setText("Status Audio:" + ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO));
        tvCell.setText("Status Call Phone:" + ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE));
        tvSendSMS.setText("Status SMS:" + ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS));
        tvLocation.setText("Status Location:" + ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    //Boton para regresar a la activity principal
    private void regresar(View view){
        Intent volver = new Intent(this, MainActivity.class);
        startActivity(volver);
    }


    //6. Solicitud del permiso de camara
    private void voidRequestPermissionsCamera(View view){
        if(ActivityCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        }
        else{
            tvResponse.setText("Permiso para Camara ya fue concedido");
        }
    }

    private void voidRequestPermissionsAudio(View view){
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_AUDIO);
        }
        else{
            tvResponse.setText("Permiso para Record Audio ya fue concedido");
        }
    }

    private void voidRequestPermissionsCell(View view){
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CELL);
        }
        else{
            tvResponse.setText("Permiso para Call Phone ya fue concedido");
        }
    }

    private void voidRequestPermissionsSMS(View view){
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE_SENDSMS);
        }
        else{
            tvResponse.setText("Permiso para Send SMS ya fue concedido");
        }
    }

    private void voidRequestPermissionsLocation(View view){
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
        }
        else{
            tvResponse.setText("Permiso para Location ya fue concedido");
        }
    }

    //7. Gestion de respuesta del usuario con respecto a la solicitud del permiso
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //tvResponse.setText("Permiso concedido: " + permissions[0]);

        if(requestCode == 1){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                new AlertDialog.Builder(this)
                        .setTitle("Box Permission")
                        .setMessage("You denied the permission Camera")
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
            else{
                actualizarPermisos();
                Toast.makeText(this, "Usted ha otorgado los permisos Camera", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 2){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                new AlertDialog.Builder(this)
                        .setTitle("Box Permission")
                        .setMessage("You denied the permission Record Audio")
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
            else{
                actualizarPermisos();
                Toast.makeText(this, "Usted ha otorgado los permisos Record Audio", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 3) {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                new AlertDialog.Builder(this)
                        .setTitle("Box Permission")
                        .setMessage("You denied the permission Call Phone")
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
            else{
                actualizarPermisos();
                Toast.makeText(this, "Usted ha otorgado los permisos Call Phone", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 4) {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                new AlertDialog.Builder(this)
                        .setTitle("Box Permission")
                        .setMessage("You denied the permission Send SMS")
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
            else{
                actualizarPermisos();
                Toast.makeText(this, "Usted ha otorgado los permisos para Send SMS", Toast.LENGTH_LONG).show();
            }
        }
        else{
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                new AlertDialog.Builder(this)
                        .setTitle("Box Permission")
                        .setMessage("You denied the permission Location")
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
            else{
                actualizarPermisos();
                Toast.makeText(this, "Usted ha otorgado los permisos para Location", Toast.LENGTH_LONG).show();
            }
        }
    }
}