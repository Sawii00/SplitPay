package com.sawii.splitpay.Database;

import java.util.ArrayList;

public class Account {
    ArrayList<Member> members;
    String name;
    public Account(String name, ArrayList<Member> members) {
        this.members = members;
        this.name = name;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public String getName() {
        return name;
    }

    public String membersToString(){
        String result = new String();
        for (Member m: members) {
            result += m.getName();
        }
        return result;
    }
}
