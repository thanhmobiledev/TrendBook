package com.thanhnguyen.angihomnay.Func;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.thanhnguyen.angihomnay.CustomAdapter.AdapterHienThiChiTiet;
import com.thanhnguyen.angihomnay.CustomAdapter.AdapterHienThiLoaiMonAnThucDon;
import com.thanhnguyen.angihomnay.DAO.BanAnDAO;
import com.thanhnguyen.angihomnay.DTO.DiaDiemHot;
import com.thanhnguyen.angihomnay.DTO.LoaiHinhDTO;
import com.thanhnguyen.angihomnay.DTO.NhaHangDTO;
import com.thanhnguyen.angihomnay.FragmentApp.HTDanhGia;
import com.thanhnguyen.angihomnay.R;
import com.thanhnguyen.angihomnay.config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class mxh extends AppCompatActivity implements View.OnClickListener {
    public static int REQUEST_CODE_MOHINH = 123;
    DatabaseReference mData = FirebaseDatabase.getInstance("https://smartorder-8f077.firebaseio.com/").getReference();
    ListView lvBanAn;
    LinearLayout llfb;
    QLDanhGiaAdapter adapter;
    ArrayList<HTDanhGia> arrayList= new ArrayList<HTDanhGia>();
    String ten,diem,tg,nd,hinh, cmt="Chưa có",share="Chia sẻ ngay", like="0";
    String link="trong";
    String tendnn="Khách";
    ImageView imhinh, imgBack;
    EditText ednd;
    Button btndanhgia;
    ViewFlipper viewfl;
    private PublisherAdView adView;
    String mabaidang, tendn;
    LinearLayout lldangbai, llreview;
    List<DiaDiemHot> loaiMonAnDTOs;
    List<DiaDiemHot> listBA = new ArrayList<DiaDiemHot>();
    String URL_GET_PRODUCT= config.domain +"android/getloaihinh_hot.php";
    int mark=0;
    boolean st1=false, st2=false, st3=false, st4=false, st5=false;
    ImageView star1, star2, star3, star4,star5;
    SharedPreferences sharedpreferences;
    String manh;
    int flag=0;

    ArrayList<HTDanhGia> list;
    static boolean active = false;

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }


    public void LayTatCaLoaiMon(){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_PRODUCT, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(mxh.this, response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            //Toast.makeText(mxh.this, response.toString(), Toast.LENGTH_SHORT).show();
                            // Convert json array to jsonobject
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                DiaDiemHot product = new DiaDiemHot();
                                product.setMaLoai(item.getInt("maloai"));
                                product.setTenLoai(item.getString("tenloai"));
                                product.setHinhAnh(item.getString("hinhanh"));
                                product.setMaTinh(item.getString("matinh"));
                                product.setMaHuyen(item.getString("mahuyen"));
                                listBA.add(product);
                            }
                            HienThiLoaiMon();
                        } catch (Exception ex) {
//                            Toast.makeText(mxh.this, ""+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                        //                  loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mxh.this, ""+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(mxh.this);
        requestQueue.add(request);

    }

    final List<NhaHangDTO> listNew = new ArrayList<NhaHangDTO>();
    String urlbaidang= config.domain +"android/getnhahang.php";
    String urlbaidang1= config.domain +"android/getnhahangtheotinh.php";
    public List<NhaHangDTO> getbd(){
        listNew.clear();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlbaidang ,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                NhaHangDTO bd = new NhaHangDTO();
                                bd.setManh(item.getString("manh"));
                                bd.setTennh(item.getString("tennh"));
                                bd.setTinh(item.getString("tinh"));
                                bd.setHuyen(item.getString("huyen"));
                                bd.setXa(item.getString("xa"));
                                bd.setMota(item.getString("mota"));
                                bd.setLoaihinh(item.getString("loainh"));
                                bd.setHinhanh(item.getString("hinhanh"));
                                bd.setTrangthai(item.getString("trangthai"));
                                bd.setBanonline(item.getString("banonline"));
                                bd.setNgaydang(item.getString("thoigiandang"));
                                listNew.add(bd);
                            }
                            HienThiDanhSachBanAn();
                        } catch (Exception ex) {
                            Toast.makeText(mxh.this, "Bạn cần bật 3G/4G hoặc Wifi để ứng dụng kết nối tới Server", Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mxh.this, "Bạn cần bật 3G/4G hoặc Wifi để ứng dụng truy xuất dữ liệu từ server!", Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
        return listNew;

    }
    public List<NhaHangDTO> getbd1(String matinh){
        listNew.clear();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlbaidang1+"?matinh="+ matinh ,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                NhaHangDTO bd = new NhaHangDTO();
                                bd.setManh(item.getString("manh"));
                                bd.setTennh(item.getString("tennh"));
                                bd.setTinh(item.getString("tinh"));
                                bd.setHuyen(item.getString("huyen"));
                                bd.setXa(item.getString("xa"));
                                bd.setMota(item.getString("mota"));
                                bd.setLoaihinh(item.getString("loainh"));
                                bd.setHinhanh(item.getString("hinhanh"));
                                bd.setTrangthai(item.getString("trangthai"));
                                bd.setBanonline(item.getString("banonline"));
                                bd.setNgaydang(item.getString("thoigiandang"));
                                listNew.add(bd);
                            }
                            HienThiDanhSachBanAn();

                        } catch (Exception ex) {
                            Toast.makeText(mxh.this, ex.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mxh.this, error.toString(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(mxh.this, "Bạn cần bật 3G/4G hoặc Wifi để ứng dụng truy xuất dữ liệu từ server!", Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
        return listNew;

    }


    GridView gvHienThiBanAn;
    GridView gv_hot;
    List<NhaHangDTO> banAnDTOList;
    BanAnDAO banAnDAO;
    AdapterHienThiChiTiet adapterHienThiChiTiet;
    int maquyen = 0;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        tendnn=intent.getStringExtra("tendn");
        manh = "MXH_01";
        //Toast.makeText(this, manh, Toast.LENGTH_LONG).show();
        setContentView(R.layout.mxhnew);
        imhinh= findViewById(R.id.imHinhDG);
        ednd=findViewById(R.id.motachitiet);
        btndanhgia=findViewById(R.id.btndanhcmngia);
        btndanhgia.setEnabled(true);
        btndanhgia.setOnClickListener(this);
        imgBack= findViewById(R.id.btnBack);
        imhinh.setOnClickListener(this);
        star1= findViewById(R.id.star1);
        star2= findViewById(R.id.star2);
        star3= findViewById(R.id.star3);
        star4= findViewById(R.id.star4);
        star5= findViewById(R.id.star5);
        llfb= findViewById(R.id.llfb);
        llfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mxh.this, mxhdangbai.class);
                i.putExtra("manh", manh);
                i.putExtra("tendn", tendnn);
                startActivity(i);
            }
        });
        try{
            Intent i= getIntent();
            tendnn=i.getStringExtra("tendn");
        }catch (Exception e)
        {

        }

        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(st1==false )
                {
                    mark=1;
                    star1.setImageResource(R.drawable.favourites);
                    st1=true;
                }
                if(st1==true)
                {
                    mark=1;
                    star2.setImageResource(R.drawable.star);
                    star3.setImageResource(R.drawable.star);
                    star4.setImageResource(R.drawable.star);
                    star5.setImageResource(R.drawable.star);
                    st1=false;
                }

            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(st2==false)
                {
                    mark=2;
                    star1.setImageResource(R.drawable.favourites);
                    star2.setImageResource(R.drawable.favourites);
                    st2=true;
                }
                if(st2==true)
                {
                    mark=2;
                    star3.setImageResource(R.drawable.star);
                    star4.setImageResource(R.drawable.star);
                    star5.setImageResource(R.drawable.star);
                    st2=false;
                }
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(st3==false)
                {
                    mark=3;
                    star1.setImageResource(R.drawable.favourites);
                    star2.setImageResource(R.drawable.favourites);
                    star3.setImageResource(R.drawable.favourites);
                    st3=true;
                }
                if(st3==true)
                {
                    mark=3;
                    star4.setImageResource(R.drawable.star);
                    star5.setImageResource(R.drawable.star);
                    st3=false;
                }
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(st4==false)
                {
                    mark=4;
                    star1.setImageResource(R.drawable.favourites);
                    star2.setImageResource(R.drawable.favourites);
                    star3.setImageResource(R.drawable.favourites);
                    star4.setImageResource(R.drawable.favourites);
                    st4=true;
                }
                if(st4==true)
                {
                    mark=4;
                    star5.setImageResource(R.drawable.star);
                    st4=false;

                }
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(st5==false)
                {
                    mark=5;
                    star1.setImageResource(R.drawable.favourites);
                    star2.setImageResource(R.drawable.favourites);
                    star3.setImageResource(R.drawable.favourites);
                    star4.setImageResource(R.drawable.favourites);
                    star5.setImageResource(R.drawable.favourites);
                }

            }
        });

        lvBanAn = (ListView) findViewById(R.id.lvBanAn);
        list = new ArrayList<>();
        try{
            addBan();
        }
        catch (Exception ex)
        {

        }
        viewfl= findViewById(R.id.qc);
        gvHienThiBanAn = (GridView) findViewById(R.id.gvHienBanAn);
        gv_hot=findViewById(R.id.gv_hot);
        lldangbai=findViewById(R.id.lldangbai);
        llreview=findViewById(R.id.llreview);
        LayTatCaLoaiMon();
        loaiMonAnDTOs = listBA;
        sharedPreferences = mxh.this.getSharedPreferences("luuquyen", Context.MODE_PRIVATE);
        maquyen = sharedPreferences.getInt("maquyen",0);
        //getbd();
        LoadLoaiHinh();
        MobileAds.initialize(mxh.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        banAnDTOList=listNew;
        banAnDAO = new BanAnDAO(mxh.this);


        lvBanAn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* mabaidang = banAnDTOList.get(position).getManh();
                tendn = banAnDTOList.get(position).getTennh();
                //Toast.makeText(this, "mabaidang: "+ mabaidang+ "tendn"+ tendn, Toast.LENGTH_SHORT).show();
                Intent iDangKy = new Intent(mxh.this, show_full.class);
                iDangKy.putExtra("manh",mabaidang);
                iDangKy.putExtra("tendn",tendn);
                startActivity(iDangKy);*/
            }
        });
        gv_hot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                manh = "MXH_"+ String.valueOf(loaiMonAnDTOs.get(position).getMaTinh());
                arrayList.clear();
                addBan();
                //getbd1(matinh);
                /*Intent iDangKy = new Intent(this, show_full.class);
                iDangKy.putExtra("manh",mabaidang);
                iDangKy.putExtra("tendn",tendn);
                startActivity(iDangKy);*/
            }
        });



    }
    public List<LoaiHinhDTO> listLoai = new ArrayList<LoaiHinhDTO>();
    public List<LoaiHinhDTO> LoadLoaiHinh(){
        String URL_GET_HUYEN= config.domain +"android/quangcao.php";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_HUYEN, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                LoaiHinhDTO product = new LoaiHinhDTO();
                                product.setMaloaihinh(item.getString("maloaihinh"));
                                product.setTenloai(item.getString("tenloaihinh"));
                                product.setHinhLoai(item.getString("hinhanhlh"));
                                listLoai.add(product);
                            }
                            ActionViewFlipper();
                        } catch (Exception ex) {
                            Toast.makeText(mxh.this, ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mxh.this, "Bạn cần bật 3G/4G hoặc Wifi để ứng dụng truy xuất dữ liệu từ server!", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
        return listLoai;
    }
    private  void ActionViewFlipper()
    {
        ArrayList<String> qcc= new ArrayList<>();
        for( int i=0; i < listLoai.size(); i++)
        {
            ImageView imageView= new ImageView(mxh.this);
            loadimageinternet(listLoai.get(i).getHinhLoai(),imageView);
            viewfl.addView(imageView);
            imageView.setScaleType(ImageView.ScaleType.MATRIX);

        }
        viewfl.startFlipping();
        viewfl.setFlipInterval(3000);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide);
        Animation animation_right = AnimationUtils.loadAnimation(this, R.anim.slide_right);
        viewfl.setInAnimation(animation);
        viewfl.setOutAnimation(animation_right);
    }
    private void loadimageinternet(String url , ImageView x){
        Picasso.get().load(url).placeholder(R.drawable.icon)
                .fit()
                .into(x, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError(Exception e) {
                    }
                });
    }
    private void HienThiDanhSachBanAn(){
        banAnDTOList = listNew;
        //Toast.makeText(this, "da vao"+ banAnDTOList.size(), Toast.LENGTH_SHORT).show();
        adapterHienThiChiTiet = new AdapterHienThiChiTiet(this, R.layout.custom_layout_hienthichitiet,banAnDTOList);
        gvHienThiBanAn.setAdapter(adapterHienThiChiTiet);
        adapterHienThiChiTiet.notifyDataSetChanged();
    }
    public void HienThiLoaiMon()
    {
        AdapterHienThiLoaiMonAnThucDon adapdater = new AdapterHienThiLoaiMonAnThucDon(mxh.this, R.layout.customlayout_hienthi_hot,listBA);
        gv_hot.setAdapter(adapdater);
        adapdater.notifyDataSetChanged();

    }

    public  void HienThi()
    {
        //Toast.makeText(this, "â "+ arrayList.size(), Toast.LENGTH_SHORT).show();
        //ArrayList
        Collections.reverse(arrayList);
        adapter= new QLDanhGiaAdapter(mxh.this, R.layout.customhtdanhgia, arrayList);
        adapter.notifyDataSetChanged();
        lvBanAn.setAdapter(adapter);
    }

    private void addBan() {
        mData.child(manh).child("DanhGia").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey().trim();
                //oast.makeText(mxh.this, key, Toast.LENGTH_SHORT).show();

                    try{
                        diem = dataSnapshot.child("diem").getValue().toString();
                        hinh = dataSnapshot.child("hinh").getValue().toString();
                        nd=dataSnapshot.child("nd").getValue().toString();
                        ten = dataSnapshot.child("ten").getValue().toString();
                        tg=dataSnapshot.child("tg").getValue().toString();
                        like=dataSnapshot.child("like").getValue().toString();
                        cmt=dataSnapshot.child("cmt").getValue().toString();
                        share=dataSnapshot.child("share").getValue().toString();
                        arrayList.add(new HTDanhGia(diem,hinh,nd,ten,tg,like, cmt, share));
                        HienThi();



                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(mxh.this, ex.toString(), Toast.LENGTH_LONG).show();
                    }



                }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey().trim();
                try{
                    diem = dataSnapshot.child("diem").getValue().toString();
                    hinh = dataSnapshot.child("hinh").getValue().toString();
                    nd=dataSnapshot.child("nd").getValue().toString();
                    ten = dataSnapshot.child("ten").getValue().toString();
                    tg=dataSnapshot.child("tg").getValue().toString();
                    like=dataSnapshot.child("like").getValue().toString();
                    cmt=dataSnapshot.child("cmt").getValue().toString();
                    share=dataSnapshot.child("share").getValue().toString();
                    arrayList.add(new HTDanhGia(diem,hinh,nd,ten,tg,like, cmt, share));
                    HienThi();

                }
                catch (Exception ex)
                {
                    Toast.makeText(mxh.this, ex.toString(), Toast.LENGTH_LONG).show();
                }




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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FirebaseStorage storage = FirebaseStorage.getInstance();

        if(REQUEST_CODE_MOHINH == requestCode){
            if(resultCode == Activity.RESULT_OK && data !=null){
                imhinh.setImageURI(data.getData());
                StorageReference storageRef = storage.getReferenceFromUrl("gs://smartorder-8f077.appspot.com"); //gs://smartorder-13eb1.appspot.com
                Calendar calendar= Calendar.getInstance();
                StorageReference mountainsRef = storageRef.child("image"+ calendar.getTimeInMillis()+ ".png");

                // Get the data from an ImageView as bytes
                Bitmap bitmap = ((BitmapDrawable) imhinh.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] dataa = baos.toByteArray();

                final UploadTask uploadTask = mountainsRef.putBytes(dataa);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(mxh.this, "Loi upload hinh", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                if (taskSnapshot.getMetadata() != null) {
                                    if (taskSnapshot.getMetadata().getReference() != null) {
                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String imageUrl = uri.toString();
                                                link=imageUrl;
                                                //MonAnDTO monAnDTO=new MonAnDTO(1,1,String.valueOf(edGiaTien.toString()), String.valueOf(edGiaTien.getText()),String.valueOf(imageUrl));
                                                //myRef.setValue(imageUrl);
                                                //Toast.makeText(DangBai.this, imageUrl, Toast.LENGTH_SHORT).show();
                                                Log.d("cc",imageUrl.toString());
                                            }
                                        });
                                    }
                                }
                            }});
                    }
                });
            }
        }
    }


    @Override
    public void onClick(View v) {
        int id= v.getId();
        switch (id) {


            case R.id.imHinhDG:
                Intent iMoHinh = new Intent();
                iMoHinh.setType("image/*");
                iMoHinh.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(iMoHinh, "Chọn hình ảnh"), REQUEST_CODE_MOHINH);
                ;
                break;
            case R.id.btndanhcmngia:
            {
                if( String.valueOf(link)=="trong")
                {
                    Toast.makeText(this, "Hãy thêm một tấm hình quán ăn bạn mới ghé nào ^^", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    DateFormat dateFormatter = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
                    dateFormatter.setLenient(false);
                    Date today = new Date();
                    String ngaygoi = dateFormatter.format(today);
                    listdanhgia x = new listdanhgia(mark+"", tendnn, ednd.getText().toString(), link,ngaygoi, like, cmt, share);

                    mData.child(manh).child("DanhGia").push().setValue(x);
                    ednd.setText("");
                    btndanhgia.setEnabled(false);
                }


            }
        }

    }
}
