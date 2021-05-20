package com.example.raiq.model;

public class ScanHisrory {

    private String URL;
    private String Address;
    private String Name;
    private long ScanTime;

    public ScanHisrory(String URL, String address, String name, long scanTime) {
        this.URL = URL;
        Address = address;
        Name = name;
        ScanTime = scanTime;
    }

    public long getScanTime() {
        return ScanTime;
    }

    public void setScanTime(long scanTime) {
        ScanTime = scanTime;
    }

    public ScanHisrory() {
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
