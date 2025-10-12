package com.example.treinoorganizado;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class InformacoesAdicionais extends AppCompatActivity {

    private EditText rAltura, rPeso, rObjetivo, rIdade, rPeito, rCintura, rQuadril, rBraco, rCoxa;
    private RadioGroup sexoGroup;
    private Button btnSalvar;
    private TextView textoDados;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informacoes_adicionais);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        textoDados = findViewById(R.id.textoDados);
        rAltura = findViewById(R.id.rAltura);
        rPeso = findViewById(R.id.rPeso);
        rObjetivo = findViewById(R.id.rObjetivo);
        rIdade = findViewById(R.id.rIdade);
        rPeito = findViewById(R.id.rPeito);
        rCintura = findViewById(R.id.rCintura);
        rQuadril = findViewById(R.id.rQuadril);
        rBraco = findViewById(R.id.rBraco);
        rCoxa = findViewById(R.id.rCoxa);
        sexoGroup = findViewById(R.id.sexoGroup);
        btnSalvar = findViewById(R.id.btnSalvar);

        textoDados.setOnClickListener(v -> {
            Intent intent = new Intent(InformacoesAdicionais.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnSalvar.setOnClickListener(v -> salvarDados());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // ============================================================
    // Método principal de salvamento
    // ============================================================
    private void salvarDados() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show();
            return;
        }

        String userID = user.getUid();

        // ----------- Captura os valores dos campos -----------------
        String alturaS = rAltura.getText().toString().trim();
        String pesoS = rPeso.getText().toString().trim();
        String objetivoS = rObjetivo.getText().toString().trim();
        String idadeS = rIdade.getText().toString().trim();
        String peitoS = rPeito.getText().toString().trim();
        String cinturaS = rCintura.getText().toString().trim();
        String quadrilS = rQuadril.getText().toString().trim();
        String bracoS = rBraco.getText().toString().trim();
        String coxaS = rCoxa.getText().toString().trim();

        int selectedId = sexoGroup.getCheckedRadioButtonId();
        RadioButton radioSelecionado = findViewById(selectedId);
        String sexoBiologico = (radioSelecionado != null) ? radioSelecionado.getText().toString() : "";

        // ============================================================
        // 1️⃣ Atualiza "dadosAtuais" no documento principal
        // ============================================================
        Map<String, Object> dadosAtuais = new HashMap<>();
        if (!objetivoS.isEmpty()) dadosAtuais.put("objetivo_treino", objetivoS);
        if (!sexoBiologico.isEmpty()) dadosAtuais.put("sexo_biologico", sexoBiologico);
        if (!alturaS.isEmpty()) dadosAtuais.put("altura", Double.parseDouble(alturaS) / 100);
        if (!pesoS.isEmpty()) dadosAtuais.put("peso", Double.parseDouble(pesoS));
        if (!idadeS.isEmpty()) dadosAtuais.put("idade", Integer.parseInt(idadeS));

        db.collection("Usuarios").document(userID)
                .set(Map.of("dadosAtuais", dadosAtuais), SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Dados atuais atualizados!", Toast.LENGTH_SHORT).show();

                    // Após atualizar o documento principal, salva o histórico
                    salvarMedidas(userID, alturaS, pesoS, idadeS, peitoS, cinturaS, quadrilS, bracoS, coxaS);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erro ao salvar dados atuais: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // ============================================================
    // 2️⃣ Salva o histórico de medidas em subcoleção
    // ============================================================
    private void salvarMedidas(String userID, String alturaS, String pesoS, String idadeS,
                               String peitoS, String cinturaS, String quadrilS, String bracoS, String coxaS) {

        Map<String, Object> medidas = new HashMap<>();

        if (!alturaS.isEmpty()) medidas.put("altura", Double.parseDouble(alturaS) / 100);
        if (!pesoS.isEmpty()) medidas.put("peso", Double.parseDouble(pesoS));
        if (!idadeS.isEmpty()) medidas.put("idade", Integer.parseInt(idadeS));
        if (!peitoS.isEmpty()) medidas.put("peito", Integer.parseInt(peitoS));
        if (!cinturaS.isEmpty()) medidas.put("cintura", Integer.parseInt(cinturaS));
        if (!quadrilS.isEmpty()) medidas.put("quadril", Integer.parseInt(quadrilS));
        if (!bracoS.isEmpty()) medidas.put("braco", Integer.parseInt(bracoS));
        if (!coxaS.isEmpty()) medidas.put("coxa", Integer.parseInt(coxaS));

        if (medidas.isEmpty()) {
            Toast.makeText(this, "Nenhum dado de medida informado, salvando mesmo assim.", Toast.LENGTH_SHORT).show();
        }


        // Adiciona um campo de data/hora legível
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault()).format(new Date());
        medidas.put("data_registro", timestamp);

        db.collection("Usuarios").document(userID)
                .collection("medidas")
                .document(timestamp)
                .set(medidas)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Histórico de medidas salvo com sucesso!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erro ao salvar histórico: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
