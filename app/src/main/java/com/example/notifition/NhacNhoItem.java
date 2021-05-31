package com.example.notifition;

public class NhacNhoItem {
    String ten, chitiet, thoigian, id;
    int trangthai, laplai, stt;

    public NhacNhoItem(String ten, String chitiet, String thoigian, int laplai, int trangthai, String id, int stt) {
        this.ten = ten;
        this.chitiet = chitiet;
        this.thoigian = thoigian;
        this.trangthai = trangthai;
        this.laplai = laplai;
        this.id =id;
        this.stt = stt;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getChitiet() {
        return chitiet;
    }

    public void setChitiet(String chitiet) {
        this.chitiet = chitiet;
    }

    public String getThoigian() {
        return thoigian;
    }

    public void setThoigian(String thoigian) {
        this.thoigian = thoigian;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

    public int getLaplai() {
        return laplai;
    }

    public void setLaplai(int laplai) {
        this.laplai = laplai;
    }
}
