package com.example.treinoorganizado;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InformacoesAdicionais extends AppCompatActivity {

    private EditText rAltura, rPeso, rObjetivo,rIdade,rPeito,rCintura,rQuadril,rBraco,rCoxa;
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

        textoDados= findViewById(R.id.textoDados);
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




        textoDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InformacoesAdicionais.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarDados();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void salvarDados(){
        FirebaseUser user= mAuth.getCurrentUser();

        if (user == null){
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show();
            return;
        }
        String userID = user.getUid();

        String alturaS = rAltura.getText().toString().trim();
        String pesoS = rPeso.getText().toString().trim();
        String objetivoS = rObjetivo.getText().toString().trim();
        String idadeS = rIdade.getText().toString().trim();
        String peitoS = rPeito.getText().toString().trim();
        String cinturaS = rCintura.getText().toString().trim();
        String quadrilS = rQuadril.getText().toString().trim();
        String bracoS = rBraco.getText().toString().trim();
        String coxaS= rCoxa.getText().toString().trim();

        int selectedId= sexoGroup.getCheckedRadioButtonId();
        RadioButton radioSelecionado = findViewById(selectedId);
        String sexoBiologico = (radioSelecionado != null) ? radioSelecionado.getText().toString() :"";


        Map<String, Object> dados = new HashMap<>();
        if (!objetivoS.isEmpty())dados.put("objetivo_treino", objetivoS);
        if (!sexoBiologico.isEmpty()) dados.put("sexo_biologico", sexoBiologico);



        db.collection("Usuarios").document(userID).update("dadosAtuais", dados)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(InformacoesAdicionais.this,"Dados atuais salvos!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(InformacoesAdicionais.this,"Erro ao salvar atuais: "+e.getMessage(),Toast.LENGTH_SHORT).show());


        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());


        Map<String, Object> medidas = new HashMap<>();
        if (!alturaS.isEmpty()) medidas.put("altura",Double.parseDouble(alturaS)/100);
        if (!pesoS.isEmpty())medidas.put("peso",Double.parseDouble(pesoS));
        if (!idadeS.isEmpty())medidas.put("idade",Integer.parseInt(idadeS));

        if (!peitoS.isEmpty()) medidas.put("peito", Integer.parseInt(peitoS));
        if (!cinturaS.isEmpty()) medidas.put("cintura", Integer.parseInt(cinturaS));
        if (!quadrilS.isEmpty()) medidas.put("quadril", Integer.parseInt(quadrilS));
        if (!bracoS.isEmpty()) medidas.put("braco", Integer.parseInt(bracoS));
        if (!coxaS.isEmpty()) medidas.put("coxa", Integer.parseInt(coxaS));

        db.collection("Usuarios").document(userID)
                .collection("medidas")
                .document(timestamp)  // cada medida será um documento com data/hora
                .set(medidas)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(InformacoesAdicionais.this,"Histórico atualizado!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(InformacoesAdicionais.this,"Erro no histórico: "+e.getMessage(),Toast.LENGTH_SHORT).show());


    }
}