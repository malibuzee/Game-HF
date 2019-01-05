package com.malibuzee.gamehfv1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements LocationListener {
    protected LocationManager locationManager;
    TextView coordenadas;
    TextView direccion;
    Button button;
    //Button btn_hacerfoto;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1000, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1000, this);

        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.btnEnviar);

        //Relacionamos con el XML para TOMAR FOTO
        img = (ImageView)this.findViewById(R.id.imgMostrar);
        //btn_hacerfoto = (Button) this.findViewById(R.id.btnEnviar);


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    TextView co = (TextView) findViewById(R.id.txtCoordenadas);
                    TextView di = (TextView) findViewById(R.id.txtDireccion);
                    String messageToSend = co.getText().toString();
                    String messageToSend2 = di.getText().toString();
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                Manifest.permission.SEND_SMS)) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.SEND_SMS}, 1);
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.SEND_SMS}, 1);
                        }
                    }else {
                        String number = "7471441423";

                        SmsManager.getDefault().sendTextMessage(number, null, messageToSend2 + "n" +
                                messageToSend, null, null);
                    }
                    Toast.makeText(getApplicationContext(), "Mensaje Enviado!",
                            Toast.LENGTH_LONG).show();
                }   catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Fallo el envio!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    }
            }
        });

        //PARA TOMAR FOTO
        //Añadimos el Listener Boton
        //btn_hacerfoto.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        //Creamos el Intent para llamar a la Camara
        //      Intent cameraIntent = new Intent(
        //              android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //      //Creamos una carpeta en la memeria del terminal
        //      File imagesFolder = new File(
        //              Environment.getExternalStorageDirectory(), "AndroidFacil");
        //      imagesFolder.mkdirs();
        //      //añadimos el nombre de la imagen
        //      File image = new File(imagesFolder, "foto.jpg");
        //      Uri uriSavedImage = Uri.fromFile(image);
        //      //Le decimos al Intent que queremos grabar la imagen
        //      cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        //      //Lanzamos la aplicacion de la camara con retorno (forResult)
        //      startActivityForResult(cameraIntent, 1);
        //  }
        //});


    }


    //@Override
    //protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Comprovamos que la foto se a realizado
    //    if (requestCode == 1 && resultCode == RESULT_OK) {
            //Creamos un bitmap con la imagen recientemente
            //almacenada en la memoria
    //        Bitmap bMap = BitmapFactory.decodeFile(
    //              Environment.getExternalStorageDirectory()+
    //              "/AndroidFacil/"+"foto.jpg");
            //Añadimos el bitmap al imageView para
            //mostrarlo por pantalla
    //      img.setImageBitmap(bMap);
    //  }
    //}




    @Override
    public void onLocationChanged(Location location) {
        coordenadas = (TextView) findViewById(R.id.txtCoordenadas);
        direccion = (TextView) findViewById(R.id.txtDireccion);
        coordenadas.setText("Mi ubicacion actual es: " + "n Lat = "
                + location.getLatitude() + "n Long = " + location.getLongitude());
        this.setLocation(location);
    }
    public void setLocation(Location location) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        location.getLatitude(), location.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    direccion.setText("Mi direccion es: n"
                            + DirCalle.getAddressLine(0));
                }

            } catch (IOException e) {
                e.printStackTrace();
                }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

}
