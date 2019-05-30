package br.edu.ifc.videira.crud.dao;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class configFirebase {

    private static DatabaseReference referFirebase;
    private static FirebaseAuth auth;

    public static DatabaseReference getfireBase (){
        if (referFirebase == null){
            referFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referFirebase;
    }

    public static FirebaseAuth getFirebaseAuth(){
        if (auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
}
