package com.sawii.splitpay.Utils;

import com.sawii.splitpay.Database.Member;

import java.util.ArrayList;

public class Utils {

    public static ArrayList<Member> splitMembers(String string){
        String[] array= string.split(",");
        ArrayList<Member> result = new ArrayList<>();
        for (String s: array) {
            Member member = new Member(s);
            result.add(member);
        }

        return result;
    }

}
