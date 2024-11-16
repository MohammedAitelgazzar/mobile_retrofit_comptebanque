package com.example.spring_restcontroller_banuqe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spring_restcontroller_banuqe.beans.Compte;

import java.util.List;

public class CompteAdapter extends RecyclerView.Adapter<CompteAdapter.CompteViewHolder> {

    private List<Compte> comptes;

    public CompteAdapter(List<Compte> comptes) {
        this.comptes = comptes;
    }

    @NonNull
    @Override
    public CompteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new CompteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompteViewHolder holder, int position) {
        Compte compte = comptes.get(position);
        holder.text1.setText("Type: " + compte.getTypeCompte());
        holder.text2.setText("Solde: " + compte.getSolde());
    }

    @Override
    public int getItemCount() {
        return comptes != null ? comptes.size() : 0;
    }

    public void setComptes(List<Compte> comptes) {
        this.comptes = comptes;
        notifyDataSetChanged();
    }

    static class CompteViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2;

        public CompteViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}
