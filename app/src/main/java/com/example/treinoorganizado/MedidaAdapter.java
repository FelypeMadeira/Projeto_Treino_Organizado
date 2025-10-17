package com.example.treinoorganizado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MedidaAdapter extends RecyclerView.Adapter<MedidaAdapter.ViewHolder>{
    private List<Medida> lista;
    private Context context;


    public MedidaAdapter(Context context, List<Medida> lista){
        this.context=context;
        this.lista= lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(context).inflate(R.layout.item_medida, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Medida atual = lista.get(position);

        holder.txtData.setText("Data: "+atual.data_registro);
        holder.txtPeso.setText("Peso: "+atual.peso + " kg");
        holder.txtPeito.setText("Peito: "+atual.peso + " cm");
        holder.txtCintura.setText("Cintura: "+atual.cintura+" cm");
        holder.txtBraco.setText("Braço: "+atual.braco+" cm");
        holder.txtCoxa.setText("Coxa: "+atual.braco+" cm");
        holder.txtQuadril.setText("Quadril: "+atual.braco+" cm");

        if (position < lista.size() - 1) {
            Medida anterior = lista.get(position + 1);

            atual.peso_evolucao = comparar(atual.peso, anterior.peso);
            atual.peito_evolucao = comparar(atual.peito, anterior.peito);
            atual.cintura_evolucao = comparar(atual.cintura, anterior.cintura);
            atual.braco_evolucao = comparar(atual.braco, anterior.braco);
            atual.coxa_evolucao = comparar(atual.coxa, anterior.coxa);
            atual.quadril_evolucao = comparar(atual.quadril, anterior.quadril);

        } else {
            // Primeira medida não tem comparação
            atual.peso_evolucao = "stable";
            atual.peito_evolucao = "stable";
            atual.cintura_evolucao = "stable";
            atual.braco_evolucao="stable";
            atual.coxa_evolucao= "stable";
            atual.quadril_evolucao="stable";
        }

        // Atualiza ícones
        setIcon(holder.iconPeso, atual.peso_evolucao);
        setIcon(holder.iconPeito, atual.peito_evolucao);
        setIcon(holder.iconCintura, atual.cintura_evolucao);
        setIcon(holder.iconBraco, atual.braco_evolucao);
        setIcon(holder.iconCoxa, atual.coxa_evolucao);
        setIcon(holder.iconQuadril, atual.quadril_evolucao);

    }
    private String comparar(double atual, double anterior) {
        if (atual > anterior) return "up";
        else if (atual < anterior) return "down";
        else return "stable";
    }



    private void setIcon(ImageView icon, String evolucao) {
        if (evolucao == null) return;
        switch (evolucao) {
            case "up":
                icon.setImageResource(R.drawable.ic_arrow_up);
                icon.setColorFilter(ContextCompat.getColor(context, R.color.meu_vermelho));
                break;

            case "down":
                icon.setImageResource(R.drawable.ic_arrow_down);
                icon.setColorFilter(ContextCompat.getColor(context, R.color.meu_vermelho));
                break;

            case "stable":
            default:
                icon.setImageResource(R.drawable.ic_arrow_right);
                icon.setColorFilter(ContextCompat.getColor(context, R.color.meu_vermelho));
                break;
        }
    }

    @Override
    public int getItemCount(){
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtData, txtPeso, txtPeito,txtCintura,txtBraco,txtCoxa, txtQuadril;
        ImageView iconPeso, iconPeito, iconCintura,iconBraco,iconCoxa,iconQuadril;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            txtData = itemView.findViewById(R.id.txtData);
            txtPeso = itemView.findViewById(R.id.txtPeso);
            txtPeito = itemView.findViewById(R.id.txtPeito);
            txtCintura = itemView.findViewById(R.id.txtCintura);
            txtBraco = itemView.findViewById(R.id.txtBraco);
            txtQuadril= itemView.findViewById(R.id.txtQuadril);
            txtCoxa = itemView.findViewById(R.id.txtCoxa);
            iconPeso = itemView.findViewById(R.id.iconPeso);
            iconPeito = itemView.findViewById(R.id.iconPeito);
            iconCintura= itemView.findViewById(R.id.iconCintura);
            iconBraco= itemView.findViewById(R.id.iconBraco);
            iconCoxa= itemView.findViewById(R.id.iconCoxa);
            iconQuadril= itemView.findViewById(R.id.iconQuadril);

        }
    }
}
