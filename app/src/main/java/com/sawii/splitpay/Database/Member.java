package com.sawii.splitpay.Database;

import java.util.ArrayList;

public class Member {
    private String name;
    private ArrayList<Expense> payments = new ArrayList<>();
    public Member(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Expense> getPayments() {
        return payments;
    }
    public void addPayment(Double amount, String description){
        payments.add(new Expense(amount, this, description));
    }
    public Double getTotal(){
        Double result = 0.0;
        for (Expense i: payments) {
            result+=i.getAmount();
        }
        return result;
    }



}
