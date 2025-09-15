package com.example.treinoorganizado;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class Register extends AppCompatActivity {

    EditText nomeCompleto,emailRegistro,senhaRegistro ;
    Button registrarBtn;
    TextView registroTexto, textViewError;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        nomeCompleto = findViewById(R.id.nomeCompleto);
        emailRegistro= findViewById(R.id.emailRegistro);
        senhaRegistro= findViewById(R.id.senhaRegistro);

        registrarBtn = findViewById(R.id.registrarBtn);
        registroTexto= findViewById(R.id.registroTexto);


        registroTexto.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        });

        fAuth = FirebaseAuth.getInstance();







        registrarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailRegistro.getText().toString().trim();
                String senha = senhaRegistro.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    emailRegistro.setError("Email é necessário");
                    return;
                }
                if (TextUtils.isEmpty(senha)){
                    senhaRegistro.setError("Senha é necessária");
                    return;
                }
                if (senha.length()<6){
                    senhaRegistro.setError("Senha precisa ser >=6");
                }

                //Registrar o cliente no firebase
                fAuth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register.this,"User Criado", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else {
                            Toast.makeText(Register.this, "Error!! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
