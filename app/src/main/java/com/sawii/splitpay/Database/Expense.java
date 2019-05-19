package com.sawii.splitpay.Database;
import java.util.Random;
public class Expense {
    private double amount;
    private Member paid_by;
    private long id;
    private String description;

    public Expense(double amount, Member paid_by, String description) {
        this.amount = amount;
        this.paid_by = paid_by;
        this.description = description;
        Random rand = new Random();
        id = rand.nextLong();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Member getPaid_by() {
        return paid_by;
    }

    public void setPaid_by(Member paid_by) {
        this.paid_by = paid_by;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
