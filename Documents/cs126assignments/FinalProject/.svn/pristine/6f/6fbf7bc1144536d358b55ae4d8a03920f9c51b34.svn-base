package com.example.katieko.finalproject;

import java.util.ArrayList;

/**
 * Created by katieko on 4/18/17.
 */

public class PaymentPlan {
    private String title;
    private double billAmount;
    private double finalTotal;
    private double tip;
    private String description;
    private ArrayList<String> payers;
    private String searchCode;

    public PaymentPlan() {
        payers = new ArrayList<>();
    }

    public PaymentPlan(String title, double billAmount, double finalTotal, double tip, String description, String searchCode) {
        this.title = title;
        this.billAmount = billAmount;
        this.finalTotal = finalTotal;
        this.tip = tip;
        this.description = description;
        this.searchCode = searchCode;
        payers = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTotalAmount(double billAmount) {
        this.finalTotal = billAmount;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public double getFinalTotal() {
        return finalTotal;
    }

    public double getTip() {
        return tip;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getPayers() {
        return payers;
    }

    public String getSearchCode() {
        return searchCode;
    }
}