package com.example.spring_restcontroller_banuqe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.spring_restcontroller_banuqe.R;
import com.example.spring_restcontroller_banuqe.beans.Compte;

import java.text.SimpleDateFormat;
import java.util.List;

public class CompteAdapter extends RecyclerView.Adapter<CompteAdapter.CompteViewHolder> {

    private List<Compte> comptes;

    public CompteAdapter(List<Compte> comptes) {
        this.comptes = comptes;
    }

    @Override
    public CompteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_compte, parent, false);
        return new CompteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompteViewHolder holder, int position) {
        Compte compte = comptes.get(position);
        holder.soldeText.setText("Solde: " + compte.getSolde());
        // Vérifier si le type est null et assigner une valeur par défaut
        String type = (compte.getType() != null) ? compte.getType() : "Type non défini";
        holder.typeText.setText("Type: " + type);
        // Formater la date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        holder.dateText.setText("Date de création: " + dateFormat.format(compte.getDateCreation()));
    }


    @Override
    public int getItemCount() {
        return comptes.size();
    }

    public void setComptes(List<Compte> comptes) {
        this.comptes = comptes;
        notifyDataSetChanged();
    }
    public void addCompte(Compte compte) {
        this.comptes.add(compte);
        notifyItemInserted(comptes.size() - 1);  // Notifie l'ajout d'un nouvel élément
    }


    public static class CompteViewHolder extends RecyclerView.ViewHolder {

        TextView soldeText, typeText, dateText;

        public CompteViewHolder(View itemView) {
            super(itemView);
            soldeText = itemView.findViewById(R.id.soldeText);
            typeText = itemView.findViewById(R.id.typeText);
            dateText = itemView.findViewById(R.id.dateText);  // Ajoutez un TextView pour la date
        }
    }
}
