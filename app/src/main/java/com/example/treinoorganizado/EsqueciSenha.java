package com.example.treinoorganizado;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

public class EsqueciSenha extends AppCompatActivity {

    private EditText editEmail;
    private Button btnRecuperar;
    private TextView txtVoltarLogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);

        editEmail = findViewById(R.id.editEmail);
        btnRecuperar = findViewById(R.id.btnRecuperar);
        txtVoltarLogin = findViewById(R.id.txtVoltarLogin);
        auth = FirebaseAuth.getInstance();

        btnRecuperar.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Digite seu e-mail", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(this, "E-mail de recuperação enviado!", Toast.LENGTH_LONG).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show());
        });

        txtVoltarLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, Login.class));
            finish();
        });
    }
}
