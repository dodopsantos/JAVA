package com.example.trido.bella_flor.dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class configFirebase {

    private static DatabaseReference referFirebase;

    public static DatabaseReference getfireBase (){
        if (referFirebase == null){
            referFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referFirebase;
    }
}
