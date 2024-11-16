package com.example.spring_restcontroller_banuqe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.spring_restcontroller_banuqe.adapter.CompteAdapter;
import com.example.spring_restcontroller_banuqe.api.ApiService;
import com.example.spring_restcontroller_banuqe.beans.Compte;
import com.example.spring_restcontroller_banuqe.config.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button btnCreate;
    private EditText soldeInput;
    private RecyclerView recyclerView;
    private CompteAdapter compteAdapter;

    private Spinner typeInput;
    private ArrayAdapter<String> typeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soldeInput = findViewById(R.id.soldeInput);
        typeInput = findViewById(R.id.typeInput); // Spinner pour le type
        btnCreate = findViewById(R.id.btnCreate);
        recyclerView = findViewById(R.id.recyclerView);

        // Configuration du RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialisation de l'adaptateur
        compteAdapter = new CompteAdapter(new ArrayList<>());
        recyclerView.setAdapter(compteAdapter);

        // Initialisation du Spinner avec les types de comptes
        List<String> types = new ArrayList<>();
        types.add("EPARGNE");
        types.add("COURANT");
        typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeInput.setAdapter(typeAdapter);

        // Ajouter un écouteur au bouton pour créer un compte
        btnCreate.setOnClickListener(v -> createCompte());

        // Récupérer tous les comptes
        getAllComptes();
    }

    // Méthode pour récupérer tous les comptes
    private void getAllComptes() {
        Call<List<Compte>> call = RetrofitClient.getApi().getAllComptes();
        call.enqueue(new Callback<List<Compte>>() {
            @Override
            public void onResponse(Call<List<Compte>> call, Response<List<Compte>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    compteAdapter.setComptes(response.body());
                } else {
                    Toast.makeText(MainActivity.this, "Aucun compte trouvé.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Compte>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Méthode pour créer un compte
    private void createCompte() {
        double solde = Double.parseDouble(soldeInput.getText().toString());
        String type = typeInput.getSelectedItem().toString();

        // Créer un objet Compte avec les données
        Compte compte = new Compte();
        compte.setSolde(solde);
        compte.setType(type);
        compte.setDateCreation(new Date());  // Ajoutez la date actuelle

        Call<Compte> call = RetrofitClient.getApi().createCompte(compte);
        call.enqueue(new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful() && response.body() != null) {
                    compteAdapter.addCompte(response.body());  // Ajoutez le compte à l'adaptateur
                    Toast.makeText(MainActivity.this, "Compte ajouté avec succès !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Échec de la création du compte.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
