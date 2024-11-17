package com.example.spring_restcontroller_banuqe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.spring_restcontroller_banuqe.adapter.CompteAdapter;
import com.example.spring_restcontroller_banuqe.api.ApiService;
import com.example.spring_restcontroller_banuqe.beans.Compte;
import com.example.spring_restcontroller_banuqe.config.RetrofitClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends AppCompatActivity {
    private Button btnCreate;
    private Button btnAddCompte;
    private EditText soldeInput;
    private Spinner typeInput;
    private RecyclerView recyclerView;
    private LinearLayout formCreateCompte; // Conteneur du formulaire de création de compte
    private CompteAdapter compteAdapter;

    private ArrayAdapter<String> typeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser les vues
        soldeInput = findViewById(R.id.soldeInput);
        typeInput = findViewById(R.id.typeInput);
        btnCreate = findViewById(R.id.btnCreate);
        btnAddCompte = findViewById(R.id.btnAddCompte);
        formCreateCompte = findViewById(R.id.formCreateCompte);
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

        // Ajouter un écouteur au bouton pour afficher le formulaire
        btnAddCompte.setOnClickListener(v -> toggleForm());

        // Ajouter un écouteur au bouton pour créer un compte
        btnCreate.setOnClickListener(v -> createCompte());

        // Récupérer tous les comptes
        getAllComptes();
    }

    // Méthode pour afficher/masquer le formulaire de création de compte
    private void toggleForm() {
        if (formCreateCompte.getVisibility() == View.GONE) {
            formCreateCompte.setVisibility(View.VISIBLE);
        } else {
            formCreateCompte.setVisibility(View.GONE);
        }
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

    // Méthode pour créer un nouveau compte
    private void createCompte() {
        String solde = soldeInput.getText().toString();
        String type = typeInput.getSelectedItem().toString();

        // Vérifier que les champs ne sont pas vides
        if (solde.isEmpty()) {
            Toast.makeText(this, "Le solde est obligatoire", Toast.LENGTH_SHORT).show();
            return;
        }

        if (type.isEmpty()) {
            Toast.makeText(this, "Le type de compte est obligatoire", Toast.LENGTH_SHORT).show();
            return;
        }

        // Créer un objet Compte avec les données saisies
        Compte newCompte = new Compte();
        newCompte.setSolde(Double.parseDouble(solde));
        newCompte.setType(type);

        // Ajouter la date de création actuelle
      //  newCompte.setDateCreation(new Date());

        // Envoyer la requête POST à l'API
        Call<Compte> call = RetrofitClient.getApi().createCompte(newCompte);
        call.enqueue(new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful()) {
                    // Afficher un message de succès
                    Toast.makeText(MainActivity.this, "Compte créé avec succès", Toast.LENGTH_SHORT).show();
                    // Ajouter le nouveau compte à la liste et rafraîchir le RecyclerView
                    compteAdapter.addCompte(response.body());
                    // Masquer le formulaire après la création
                    formCreateCompte.setVisibility(View.GONE);
                } else {
                    // Gérer l'échec de la création
                    Toast.makeText(MainActivity.this, "Erreur lors de la création du compte", Toast.LENGTH_SHORT).show();
                }
            }
            private void updateCompte(Compte compte) {
                ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
                Call<Compte> call = apiService.updateCompte(compte.getIdLong(), compte);

                call.enqueue(new Callback<Compte>() {
                    @Override
                    public void onResponse(Call<Compte> call, Response<Compte> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Compte modifié avec succès", Toast.LENGTH_SHORT).show();
                            getAllComptes(); // Actualiser la liste des comptes
                        } else {
                            Toast.makeText(MainActivity.this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                        }
                    }
                    private void deleteCompte(Long compteId) {
                        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
                        Call<Void> call = apiService.deleteCompte(compteId);

                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Compte supprimé avec succès", Toast.LENGTH_SHORT).show();
                                    getAllComptes(); // Actualiser la liste des comptes
                                } else {
                                    Toast.makeText(MainActivity.this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                    @Override
                    public void onFailure(Call<Compte> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}