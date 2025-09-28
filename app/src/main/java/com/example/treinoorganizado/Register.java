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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText nomeCompleto, emailRegistro, senhaRegistro;
    Button registrarBtn;
    TextView registroTexto;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        nomeCompleto = findViewById(R.id.nomeCompleto);
        emailRegistro = findViewById(R.id.emailRegistro);
        senhaRegistro = findViewById(R.id.senhaRegistro);
        registrarBtn = findViewById(R.id.registrarBtn);
        registroTexto = findViewById(R.id.registroTexto);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        registroTexto.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        });

        registrarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailRegistro.getText().toString().trim();
                String senha = senhaRegistro.getText().toString().trim();
                String nome = nomeCompleto.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    emailRegistro.setError("Email é necessário");
                    return;
                }
                if (TextUtils.isEmpty(senha)) {
                    senhaRegistro.setError("Senha é necessária");
                    return;
                }
                if (senha.length() < 6) {
                    senhaRegistro.setError("Senha precisa ter no mínimo 6 caracteres");
                    return;
                }

                // Registrar no Firebase Auth
                fAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = fAuth.getCurrentUser().getUid();

                            // Criar objeto com dados iniciais
                            Map<String, Object> dadosUsuario = new HashMap<>();
                            dadosUsuario.put("nome", nome);
                            dadosUsuario.put("email", email);

                            // Salvar no Firestore
                            fStore.collection("Usuarios").document(userId)
                                    .set(dadosUsuario)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(Register.this, "Usuário criado!", Toast.LENGTH_SHORT).show();

                                        // Enviar para a tela de informações adicionais
                                        startActivity(new Intent(getApplicationContext(), InformacoesAdicionais.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(Register.this, "Erro ao salvar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });

                        } else {
                            Toast.makeText(Register.this, "Erro!! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
