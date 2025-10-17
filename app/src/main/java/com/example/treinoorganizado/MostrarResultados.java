package com.example.treinoorganizado;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import java.util.ArrayList;
import java.util.List;

public class MostrarResultados extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedidaAdapter adapter;
    private List<Medida> medidaList = new ArrayList<>();
    private Button voltaBtn;

    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_resultados);

        recyclerView = findViewById(R.id.recyclerMedidas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        voltaBtn = findViewById(R.id.voltaBtn);
        voltaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MostrarResultados.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        adapter = new MedidaAdapter(this, medidaList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        carregarHistorico();
    }

    private void carregarHistorico() {
        db.collection("Usuarios")
                .document(userId)
                .collection("medidas")
                .orderBy("data_registro", Query.Direction.DESCENDING) // 🔽 mais recente primeiro
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Medida> lista = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Medida m = doc.toObject(Medida.class);
                        lista.add(m);
                    }

                    MedidaAdapter adapter = new MedidaAdapter(this, lista);
                    recyclerView.setAdapter(adapter);
                });
    }
}
