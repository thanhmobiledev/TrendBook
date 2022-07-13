package com.thanhnguyen.angihomnay.Func;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thanhnguyen.angihomnay.FragmentApp.HTDanhGia;
import com.thanhnguyen.angihomnay.R;

import java.util.List;

public class QLDanhGiaAdapter extends BaseAdapter
{
    ViewHolder holder;
    Context context;
    int layout;

    List<HTDanhGia> QLBanAnx;
    public int flag=0;

    public QLDanhGiaAdapter(Context context, int layout, List<HTDanhGia> QLBanAnx) {
        this.context = context;
        this.layout = layout;
        this.QLBanAnx = QLBanAnx;
    }

    @Override
    public int getCount() {

        return QLBanAnx.size();
    }

    public class ViewHolder{

        TextView txtten, txttg, txtnoidung;
        ImageView hinhdg, diem;
        ImageView btnlike, btncmt, btnshare;
        TextView txtlike, txtcmt;
    }
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view==null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);
            holder.txtten =  view.findViewById(R.id.tennguoidang);
            holder.diem = view.findViewById(R.id.diem);
            holder.txtnoidung = view.findViewById(R.id.txtnoidung);
            holder.hinhdg =  view.findViewById(R.id.imHinhDang);
            holder.btnlike =  view.findViewById(R.id.btnlike);
            holder.btncmt =  view.findViewById(R.id.btncmt);
            holder.btnshare =  view.findViewById(R.id.btnshare);
            holder.txtlike =  view.findViewById(R.id.txt_like);
            holder.txtcmt =  view.findViewById(R.id.txtcmt);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
            holder.btnlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( flag==0)
                    {
                        holder.btnlike.setImageResource(R.drawable.heart);
                        flag=1;
                    }
                    else
                    {
                        holder.btnlike.setImageResource(R.drawable.heartok);
                        flag=0;
                    }

                }
            });
        HTDanhGia x = QLBanAnx.get(i);
        holder.txtten.setText(x.getTen());
        if ( x.getLike().equals("0"))
        {
            holder.btnlike.setImageResource(R.drawable.heart);
        }
        else {
            holder.btnlike.setImageResource(R.drawable.heartok);
        }
        if( x.getNd()=="")
        {
            holder.txtnoidung.setText(" ");
        }
        else
        {
            holder.txtnoidung.setText(x.getNd().toString());
        }
        int d=Integer.parseInt(x.getDiem().trim());
        //Toast.makeText(context, d, Toast.LENGTH_LONG).show();
        if(d==1)
        {
            holder.diem.setImageResource(R.drawable.st1);
        }
        if(d==2)
        {
            holder.diem.setImageResource(R.drawable.st2);
        }
        if(d==3)
        {
            holder.diem.setImageResource(R.drawable.st3);
        }
        if(d==4)
        {
            holder.diem.setImageResource(R.drawable.st4);
        }
        if(d==5)
        {
            holder.diem.setImageResource(R.drawable.st5);
        }
        String ha=x.getHinh().trim();
        if(String.valueOf(ha).equals("trong"))
        {
            holder.hinhdg.setVisibility(View.GONE);
        }
        else  if (!String.valueOf(ha).equals("trong")){
            loadimageinternet(ha,holder.hinhdg);
            holder.hinhdg.setVisibility(View.VISIBLE);
        }
        return view;

    }
    private void loadimageinternet(String url ,ImageView x){
        Picasso.get().load(url).placeholder(R.color.transparent)
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
}
