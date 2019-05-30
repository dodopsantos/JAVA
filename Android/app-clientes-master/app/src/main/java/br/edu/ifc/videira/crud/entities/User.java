package br.edu.ifc.videira.crud.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import br.edu.ifc.videira.crud.dao.configFirebase;

@Entity(tableName = "users",
        indices = {
            @Index(value = {"email"},
            unique = true)
        })
public class User {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "id")
    public String id;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;
    @Ignore
    public User(String email,String password) {
        this.email = email;
        this.password = password;
    }

    public User(){   }

    public User(String email,String password, String name, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public void Save() {
        DatabaseReference referenciaFirebase = configFirebase.getfireBase();
        referenciaFirebase.child("usuario").child(String.valueOf(getId())).setValue(this);
    }
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> hashMapUser = new HashMap<>();

        hashMapUser.put("id", getId());
        hashMapUser.put("email", getEmail());
        hashMapUser.put("name", getName());
        hashMapUser.put("password", getPassword());
        hashMapUser.put("phone", getPhone());

        return hashMapUser;
    }

    @Override
    public String toString() {
        return name;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
}
