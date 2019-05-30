package br.edu.ifc.videira.crud;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import br.edu.ifc.videira.crud.dao.configFirebase;
import br.edu.ifc.videira.crud.entities.User;
import br.edu.ifc.videira.crud.helper.Base64Custom;
import br.edu.ifc.videira.crud.helper.PreferencesAndroid;
import br.edu.ifc.videira.crud.helper.preferences;
import br.edu.ifc.videira.crud.view_models.UserViewModel;

public class NewUserActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";
    private UserViewModel mUserViewModel;

    //Mudeando aq//
    private FirebaseAuth auth;
    private User user;
    private int a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        final EditText txtEmail = findViewById(R.id.editTextUserEmail);
        final EditText txtPasswrod = findViewById(R.id.editTextUserPassword);
        final EditText txtName = findViewById(R.id.editTextName);
        final EditText txtTel = findViewById(R.id.editTextPhone);

        final Button button = findViewById(R.id.btnSaveUser);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(txtEmail.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    user = new User(txtEmail.getText().toString(), txtPasswrod.getText().toString(), txtName.getText().toString(), txtTel.getText().toString());

                    registerUser();
                    //finish();
                }
            }
        });
    }
    //aq tbm
    private void registerUser() {
        auth = configFirebase.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(NewUserActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {
                    Toast.makeText(NewUserActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                    String idenficadorUsuario = Base64Custom.codificarBase64(user.getEmail());
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    user.setId(idenficadorUsuario);

                    user.Save();

                    PreferencesAndroid preferenciasAndroid = new PreferencesAndroid(NewUserActivity.this);
                    preferenciasAndroid.SaveUserPreferences(idenficadorUsuario, user.getName());

                    OpenloginUser();
                }else {
                    String error = "";

                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        error = "Digite uma senha mais forte(8 caracters)";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        error = "E-mail inválido";
                    }catch (FirebaseAuthUserCollisionException e){
                        error = "E-mail já cadastrado";
                    }catch (Exception e){
                        error = "Erro ao efetuar cadastro";
                        e.printStackTrace();
                    }
                    Toast.makeText(NewUserActivity.this, "Erro: "+ error + "AD: "+ a, Toast.LENGTH_LONG).show();


                }
            }
        });
    }

    public void OpenloginUser(){
        Intent intent = new Intent(NewUserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
