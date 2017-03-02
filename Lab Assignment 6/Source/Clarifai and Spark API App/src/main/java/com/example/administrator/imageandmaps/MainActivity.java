package com.example.administrator.imageandmaps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.example.administrator.imageandmaps.R.id.button2;

import com.bilal.ClariClient.ClarifaiExample;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class MainActivity extends AppCompatActivity {

    LocationManager manager;
    LocationListener listener;
    public static Location userLocation;
    public static Uri ImageUri;
    public Geocoder geocoder;
    public static String answer;
    public static String sEncoded;
    public String x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sign Up");

        bindFunctions();

        Button btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFuzzyImage(v);
            }
        });

        Button btn3 = (Button)findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage(v);
            }
        });

    }

    private void openGPS(){
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        int permissioncheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissioncheck == PackageManager.PERMISSION_GRANTED){
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        } else {
            System.out.print("Permission Denied");
        }
    }

    private void bindFunctions(){

        Button btn = (Button)findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage(v);
            }
        });
        x="ships";
//        Button btn1 = (Button)findViewById(R.id.button5);
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openGPS();
//            }
//        });

//        Button btn2 = (Button)findViewById(R.id.button4);
//        btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                runMapIntent();
//            }
//        });


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("D",location.getLatitude() + " " + location.getLongitude());
                userLocation = location;

                geocoder = new Geocoder(getBaseContext());
                StringBuilder userAddress = new StringBuilder();

                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    Thread.sleep(100);
                    Address address = addresses.get(0);
                    userAddress = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        userAddress.append(address.getAddressLine(i)).append("\t");
                    }
                    userAddress.append(address.getCountryName()).append("\t");

                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Not Found");
                }

                TextView tv = (TextView) findViewById(R.id.textView5);
                tv.setText(userAddress);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    private void loadImage(View view){
        Intent openImage = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openImage,1);
        Log.d("d","Image Loaded");
    }

    private void loadFuzzyImage(View view){
        Intent openImage = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openImage,2);
        Log.d("d","Fuzzy Image Loaded");
    }

    private void runMapIntent(){
        Intent mapsIntent = new Intent(MainActivity.this,Maps_Activity.class);
        startActivity(mapsIntent);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {

            MainActivity.ImageUri = data.getData();

            ImageView view = (ImageView) findViewById(R.id.imageView4);
            view.setImageURI(MainActivity.ImageUri);

            Thread td = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tw = (TextView) findViewById(R.id.textView5);
                                tw.setText("Waiting...");
                            }
                        });

                        Cursor cursor = MainActivity.this.getContentResolver().query(
                                ImageUri, filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        String picturePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
                        File file = new File(picturePath);


                        answer = ClarifaiExample.clarify(file);

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tw = (TextView) findViewById(R.id.textView5);
                                tw.setText(answer);
                            }
                        });

                        Log.e("Tag", answer);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            td.start();

//            String encodedImage = encodeImage(selectedImage.getPath());
//            ImageView view = (ImageView) findViewById(R.id.imageView4);
//            view.setImageURI(selectedImage);
//            ImageUri = selectedImage;
//            Button btn2 = (Button)findViewById(R.id.button4);
//            btn2.setEnabled(true);
        }

        if(requestCode == 2){
            MainActivity.ImageUri = data.getData();

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView tw = (TextView) findViewById(R.id.textView5);
                    tw.setText("Waiting...");
                }
            });

            ImageView view = (ImageView) findViewById(R.id.imageView4);
            view.setImageURI(MainActivity.ImageUri);

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = MainActivity.this.getContentResolver().query(
                    ImageUri, filePathColumn, null, null, null);
            cursor.moveToFirst();

            String picturePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));

            encodeImage(picturePath);
        }
    }

    private void encodeImage(String path)
    {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = MainActivity.this.getContentResolver().query(
                ImageUri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        String picturePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
        File imagefile = new File(path);

        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        callHttpClient(encImage);

    }

    private void callHttpClient(String encoded){
        sEncoded = encoded;
        Thread td = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    OkHttpClient client = new OkHttpClient();
//                    RequestBody body = RequestBody.create(MediaType.parse("text/plain; charset=utf-8"),sEncoded);
//                    Request request = new Request.Builder()
//                            .url("http://192.168.0.9:8081/get_custom").method("POST",body)
//                            .build();
//
//                    Response response = client.newCall(request).execute();




                    URL url = new URL("http://192.168.0.9:8081/get_custom");
                    HttpURLConnection client = null;
                    client = (HttpURLConnection) url.openConnection();

                    client.setReadTimeout(50000);
                    client.setConnectTimeout(50000);
                    client.setRequestMethod("POST");
                    client.setDoOutput(true);
                    client.setRequestProperty("Access-Control-Allow-Origin","*");


                    OutputStream os = client.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
                    writer.write(sEncoded);
                    writer.flush();
                    answer = client.getResponseMessage().toString();
                    Log.e("tag",String.valueOf(client.getResponseCode()));
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView tw = (TextView) findViewById(R.id.textView5);
                            tw.setText(x);
                        }
                    });

                } catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        td.start();

    }
}
