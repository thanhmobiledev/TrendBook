package com.thanhnguyen.angihomnay.Func;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;
import com.thanhnguyen.angihomnay.FragmentApp.ShowTableKhach;
import com.thanhnguyen.angihomnay.config;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;
import static com.thanhnguyen.angihomnay.Func.dangnhap.MANH;
import static com.thanhnguyen.angihomnay.Func.dangnhap.MyPREFERENCES;
import static com.thanhnguyen.angihomnay.Func.dangnhap.USERNAME;

public class Qrcode extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    String url= config.domain + "android/qrcode.php";
    SharedPreferences sharedpreferences;
    DatabaseReference mData = FirebaseDatabase.getInstance("https://smartorder-8f077.firebaseio.com/").getReference();

    private ZXingScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int currentApiVersion = Build.VERSION.SDK_INT;
        if(currentApiVersion >=  Build.VERSION_CODES.M)
        {
            if(checkPermission())
            {
                Toast.makeText(getApplicationContext(), "???? ???????c c???p quy???n!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                requestPermission();
            }
        }
    }
    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }
    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "H?????ng camera v??o m?? QR ????? nh???n di???n nh?? h??ng v?? b??n ??n", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "C???p quy???n camera l???i", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("B???n ph???i c???p quy???n camera ????? qu??t m??!",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new androidx.appcompat.app.AlertDialog.Builder(Qrcode.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

public  void  luu(String ma)
{
    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.clear();
    editor.putString(USERNAME, "Khach");
    editor.putString(MANH, ma);
    //Toast.makeText(this, "ma"+ ma, Toast.LENGTH_SHORT).show();
    editor.commit();
}
    void insertFireBase(String manh, String tenban){
        mData.child(manh.toUpperCase()).child("BanAn").child(tenban).child("TinhTrang").setValue(false);
        //mData.child("BanAn").child(tenban).child("maban").setValue()
    }
    public void kiemtrama(String url, final String manh, final String tennh) {
            RequestQueue requestQueue = Volley.newRequestQueue(Qrcode.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("true")){
                                insertFireBase(manh.toUpperCase(),"KH_"+tennh);
                                luu(manh.toUpperCase());
                                //Toast.makeText(Qrcode.this, "TRUE", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Qrcode.this, ShowTableKhach.class);
                                intent.putExtra("manh",manh);
                                intent.putExtra("loa","mot");
                                intent.putExtra("tenban",tennh);

                                startActivity(intent);
                            }
                            else if (response.trim().equals("manh")){
                                Toast.makeText(Qrcode.this, "L???i m?? nh?? h??ng", Toast.LENGTH_SHORT).show();
                                tbloi1();

                            }
                            else if (response.trim().equals("roi")){
                                luu(manh.toUpperCase());
                                Toast.makeText(Qrcode.this, "Ch??o m???ng b???n quay l???i!", Toast.LENGTH_SHORT).show();
                                //tbloi2();
                                Intent intent = new Intent(Qrcode.this, ShowTableKhach.class);
                                intent.putExtra("manh",manh.toUpperCase());
                                intent.putExtra("loa","hai");
                                intent.putExtra("tenban",tennh);
                                startActivity(intent);

                            }
                            else{
                                Toast.makeText(Qrcode.this, "er"+ response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(Qrcode.this, "K???t n???i m??y ch??? th???t b???i!", Toast.LENGTH_SHORT).show();

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("manh",manh.toUpperCase());
                    params.put("tenb",tennh);
                    return params;
                }
            };

            requestQueue.add(stringRequest);


    }
    public  void tbloi()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("M?? nh?? h??ng kh??ng h???p l??? ho???c ???ng d???ng ch??a ???????c c???p nh???t");
        builder.setPositiveButton("H???y", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(Qrcode.this);
            }
        });
        builder.setNeutralButton("T???i ngay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url=config.domainqrcode;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        builder.setMessage("Update phi??n b???n m???i");
        AlertDialog alert1 = builder.create();
        alert1.show();
    }
    public  void tbloi1()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("M?? nh?? h??ng kh??ng h???p l???");
        builder.setPositiveButton("Qu??t l???i", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(Qrcode.this);
            }
        });
        builder.setNeutralButton("Xem h?????ng d???n", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url=config.domainqrcode;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        builder.setMessage("Vui l??ng qu??t m?? QR ????? g???i m??n! Xem h?????ng d???n s??? d???ng app t???i ????y");
        AlertDialog alert1 = builder.create();
        alert1.show();
    }
    public  void tbloi2()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("B??n n??y ???? c?? ng?????i ng???i r???i");
        builder.setPositiveButton("H???y", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(Qrcode.this);
            }
        });
        builder.setNeutralButton("H????ng d???n", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url=config.domainqrcode;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        builder.setMessage("Xem h?????ng d???n qu??t m?? g???i m??n");
        AlertDialog alert1 = builder.create();
        alert1.show();
    }
    @Override
    public void handleResult(Result result) {
        final String myResult = result.getText();
        try{
            String[] output = myResult.split("/");
            System.out.println(output[3]);
            System.out.println(output[4]);
            kiemtrama(url,output[3],output[4]);

        }catch (Exception ex)
        {
            Toast.makeText(this, "L???i ?????nh d???ng m??", Toast.LENGTH_SHORT).show();
            tbloi();
        }

        /*;*/
    }
}
