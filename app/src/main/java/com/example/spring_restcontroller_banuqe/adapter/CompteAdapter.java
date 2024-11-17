package com.example.spring_restcontroller_banuqe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spring_restcontroller_banuqe.R;
import com.example.spring_restcontroller_banuqe.beans.Compte;

import java.util.List;

public class CompteAdapter extends RecyclerView.Adapter<CompteAdapter.CompteViewHolder> {

    private List<Compte> comptes;
    private OnDeleteClickListener onDeleteClickListener;

    public CompteAdapter(List<Compte> comptes) {
        this.comptes = comptes;
    }

    public List<Compte> getComptes() {
        return comptes;
    }

    @NonNull
    @Override
    public CompteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_compte, parent, false);
        return new CompteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompteViewHolder holder, int position) {
        Compte compte = comptes.get(position);
        holder.soldeText.setText("Solde: " + compte.getSolde());
        String type = (compte.getType() != null) ? compte.getType() : "Type non défini";
        holder.typeText.setText("Type: " + type);

        holder.deleteButton.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(position);
            }
        });

        // Lorsque le bouton "Modifier" est cliqué
        holder.editButton.setOnClickListener(v -> {
            if (onEditClickListener != null) {
                onEditClickListener.onEditClick(compte);
            }
        });
    }


    private OnEditClickListener onEditClickListener;

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.onEditClickListener = listener;
    }

    public interface OnEditClickListener {
        void onEditClick(Compte compte);
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
        comptes.add(compte);
        notifyItemInserted(comptes.size() - 1);
    }

    public void removeCompte(int position) {
        if (position >= 0 && position < comptes.size()) {
            comptes.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public static class CompteViewHolder extends RecyclerView.ViewHolder {
        TextView soldeText, typeText;
        Button deleteButton, editButton;

        public CompteViewHolder(View itemView) {
            super(itemView);
            soldeText = itemView.findViewById(R.id.soldeText);
            typeText = itemView.findViewById(R.id.typeText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
