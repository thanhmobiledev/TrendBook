package com.thanhnguyen.angihomnay.FragmentApp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.squareup.picasso.Picasso;
import com.thanhnguyen.angihomnay.CustomAdapter.AdapterHienThiChiTiet;
import com.thanhnguyen.angihomnay.CustomAdapter.AdapterHienThiLoaiMonAnThucDon;
import com.thanhnguyen.angihomnay.DAO.BanAnDAO;
import com.thanhnguyen.angihomnay.DTO.DiaDiemHot;
import com.thanhnguyen.angihomnay.DTO.LoaiHinhDTO;
import com.thanhnguyen.angihomnay.DTO.NhaHangDTO;
import com.thanhnguyen.angihomnay.Func.dangnhap;
import com.thanhnguyen.angihomnay.R;
import com.thanhnguyen.angihomnay.config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class hot extends Fragment{
    ViewFlipper viewfl;
    private PublisherAdView adView;
    String mabaidang, tendn;
    LinearLayout lldangbai, llreview;
    List<DiaDiemHot> loaiMonAnDTOs;
    List<DiaDiemHot> listBA = new ArrayList<DiaDiemHot>();
    String URL_GET_PRODUCT= config.domain +"android/getloaihinh_hot.php";
    public void LayTatCaLoaiMon(){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_PRODUCT, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
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
//                            Toast.makeText(getActivity(), ""+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                        //                  loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), ""+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                        //Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
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
                             Toast.makeText(getActivity(), "Bạn cần bật 3G/4G hoặc Wifi để ứng dụng kết nối tới Server", Toast.LENGTH_SHORT).show();
                         }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Bạn cần bật 3G/4G hoặc Wifi để ứng dụng truy xuất dữ liệu từ server!", Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
        return listNew;

    }
    public List<NhaHangDTO> getbd1(String matinh){
        listNew.clear();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlbaidang1+"?matinh="+ matinh ,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(getActivity(), "Bạn cần bật 3G/4G hoặc Wifi để ứng dụng truy xuất dữ liệu từ server!", Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot,container,false);
        setHasOptionsMenu(true);
        viewfl= view.findViewById(R.id.qc);
        gvHienThiBanAn = (GridView) view.findViewById(R.id.gvHienBanAn);
        gv_hot=view.findViewById(R.id.gv_hot);
        lldangbai=view.findViewById(R.id.lldangbai);
        llreview=view.findViewById(R.id.llreview);
        LayTatCaLoaiMon();
        loaiMonAnDTOs = listBA;
        sharedPreferences = getActivity().getSharedPreferences("luuquyen", Context.MODE_PRIVATE);
        maquyen = sharedPreferences.getInt("maquyen",0);
        getbd();
        LoadLoaiHinh();
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adView = view.findViewById(R.id.ad_view);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        adView.loadAd(adRequest);

        banAnDTOList=listNew;
        banAnDAO = new BanAnDAO(getActivity());
        lldangbai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.thanhnguyen.timtro"));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {

                }
            }
        });
        llreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getContext(), dangnhap.class);
                startActivity(i);
            }
        });
        gvHienThiBanAn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mabaidang = banAnDTOList.get(position).getManh();
                tendn = banAnDTOList.get(position).getTennh();
                //Toast.makeText(getContext(), "mabaidang: "+ mabaidang+ "tendn"+ tendn, Toast.LENGTH_SHORT).show();
                Intent iDangKy = new Intent(getContext(), show_full.class);
                iDangKy.putExtra("manh",mabaidang);
                iDangKy.putExtra("tendn",tendn);
                startActivity(iDangKy);
            }
        });
        gv_hot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String matinh = String.valueOf(loaiMonAnDTOs.get(position).getMaTinh());
                Log.d("thanhnv", matinh);
                getbd1(matinh);
                /*Intent iDangKy = new Intent(getContext(), show_full.class);
                iDangKy.putExtra("manh",mabaidang);
                iDangKy.putExtra("tendn",tendn);
                startActivity(iDangKy);*/
            }
        });


        return view;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu, menu);
    }



    public List<LoaiHinhDTO> listLoai = new ArrayList<LoaiHinhDTO>();
    public List<LoaiHinhDTO> LoadLoaiHinh(){
        String URL_GET_HUYEN= config.domain +"android/quangcao.php";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_HUYEN, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getContext(), "Bạn cần bật 3G/4G hoặc Wifi để ứng dụng truy xuất dữ liệu từ server!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Bạn cần bật 3G/4G hoặc Wifi để ứng dụng truy xuất dữ liệu từ server!", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
        return listLoai;
    }
    private  void ActionViewFlipper()
{
    ArrayList<String> qcc= new ArrayList<>();
     for( int i=0; i < listLoai.size(); i++)
    {
        ImageView imageView= new ImageView(getActivity());
        loadimageinternet(listLoai.get(i).getHinhLoai(),imageView);
        viewfl.addView(imageView);
        imageView.setScaleType(ImageView.ScaleType.MATRIX);

    }
    viewfl.startFlipping();
    viewfl.setFlipInterval(3000);
    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide);
    Animation animation_right = AnimationUtils.loadAnimation(getContext(), R.anim.slide_right);
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
        //Toast.makeText(getContext(), "da vao"+ banAnDTOList.size(), Toast.LENGTH_SHORT).show();
        adapterHienThiChiTiet = new AdapterHienThiChiTiet(getContext(), R.layout.custom_layout_hienthichitiet,banAnDTOList);
        gvHienThiBanAn.setAdapter(adapterHienThiChiTiet);
        adapterHienThiChiTiet.notifyDataSetChanged();
    }
    public void HienThiLoaiMon()
    {
        AdapterHienThiLoaiMonAnThucDon adapdater = new AdapterHienThiLoaiMonAnThucDon(getActivity(), R.layout.customlayout_hienthi_hot,listBA);
        gv_hot.setAdapter(adapdater);
        adapdater.notifyDataSetChanged();

    }


}
