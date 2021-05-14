package com.example.notifition;

public class monhoc {
    private String ten, phong, thu, giohoc, id;
    private String stt;

    public monhoc(String id, String ten, String phong, String thu, String giohoc, String stt) {
        this.ten = ten;
        this.phong = phong;
        this.thu = thu;
        this.giohoc = giohoc;
        this.id = id;
        this.stt = stt;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
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

    public String getPhong() {
        return phong;
    }

    public void setPhong(String phong) {
        this.phong = phong;
    }

    public String getThu() {
        return thu;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }

    public String getGiohoc() {
        return giohoc;
    }

    public void setGiohoc(String giohoc) {
        this.giohoc = giohoc;
    }
}
