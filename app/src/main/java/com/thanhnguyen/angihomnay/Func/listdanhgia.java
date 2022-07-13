package com.thanhnguyen.angihomnay.Func;

public class listdanhgia {
    String diem, ten,nd, hinh, tg, like, cmt, share;

    public String getDiem() {
        return diem;
    }

    public void setDiem(String diem) {
        this.diem = diem;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public String getTg() {
        return tg;
    }

    public void setTg(String tg) {
        this.tg = tg;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public listdanhgia() {
    }

    public listdanhgia(String diem, String ten, String nd, String hinh, String tg, String like, String cmt, String share) {
        this.diem = diem;
        this.ten = ten;
        this.nd = nd;
        this.hinh = hinh;
        this.tg = tg;
        this.like = like;
        this.cmt = cmt;
        this.share = share;
    }
}
