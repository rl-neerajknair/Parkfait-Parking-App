package com.example.parkingappfrags_v2;

public class DaySalesData {
    String day;
    int sales;

    public DaySalesData(String day, int sales) {
        this.day = day;
        this.sales = sales;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }
}
