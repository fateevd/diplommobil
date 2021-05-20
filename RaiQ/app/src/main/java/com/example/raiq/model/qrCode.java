package com.example.raiq.model;

public class qrCode {

    private String Address;
    private String De;
    private String Name;
    private String URl;

    public qrCode(String address, String de, String name, String URl) {
        Address = address;
        De = de;
        Name = name;
        this.URl = URl;
    }

    public qrCode() {
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDe() {
        return De;
    }

    public void setDe(String de) {
        De = de;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getURl() {
        return URl;
    }

    public void setURl(String URl) {
        this.URl = URl;
    }
}
