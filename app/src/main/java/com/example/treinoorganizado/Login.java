package com.example.treinoorganizado;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText email, senha;
    Button loginBtn;
    TextView loginTexto, txtEsqueciSenha ;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        loginBtn = findViewById(R.id.loginBtn);
        loginTexto=findViewById(R.id.loginTexto);
        fAuth = FirebaseAuth.getInstance();

        loginTexto.setOnClickListener(v -> {
            Intent register = new Intent(Login.this, Register.class);
            startActivity(register);
        });
        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        txtEsqueciSenha = findViewById(R.id.txtEsqueciSenha);

       txtEsqueciSenha.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, EsqueciSenha.class);
            startActivity(intent);
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_login = email.getText().toString().trim();
                String senha_login = senha.getText().toString().trim();
                if (TextUtils.isEmpty(email_login)){
                    email.setError("Email é necessário");
                    return;
                }
                if (TextUtils.isEmpty(senha_login)){
                    senha.setError("Senha é necessária");
                    return;
                }
                if (senha_login.length()<6){
                    senha.setError("Senha precisa ser >=6");
                }
                fAuth.signInWithEmailAndPassword(email_login,senha_login).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"Login feito com Sucesso!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else {
                            Toast.makeText(Login.this, "Error!! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });
    }
}