package br.edu.ifc.videira.crud.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class preferences {

    private Context context;
    private SharedPreferences preferences;
    private String name_File = "ProjectFirebase.preferences";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String ID_KEY = "idUserLogged";
    private final String NAME_KEY = "nameUserLogged";

    public preferences(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(name_File, MODE);

        editor = preferences.edit();
    }

    public void SaveUserPreferences(String userId, String userName){
        editor.putString(ID_KEY, userId);
        editor.putString(NAME_KEY, userName);
        editor.commit();
    }

    public String getIdentifier(){
        return preferences.getString(ID_KEY,null);
    }

    public String getName(){
        return preferences.getString(NAME_KEY,null);
    }

}
