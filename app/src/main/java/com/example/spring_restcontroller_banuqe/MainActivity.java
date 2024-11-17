package com.example.spring_restcontroller_banuqe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    private LinearLayout formCreateCompte;
    private CompteAdapter compteAdapter;

    private ArrayAdapter<String> typeAdapter;

    private Long compteIdToUpdate = null;

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
        Spinner spinnerType = findViewById(R.id.spinnerType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.jsonXmlOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                boolean useJson = position == 0;  // Position 0 -> JSON, Position 1 -> XML
                updateApiService(useJson);  // Méthode pour mettre à jour le service API
            }

            private void updateApiService(boolean useJson) {
                // Exemple d'utilisation du service API en fonction du type sélectionné
                ApiService apiService = RetrofitClient.getApi(useJson); // Choisit l'API en fonction du type

                // Par exemple, vous pouvez appeler `getAllComptes()` ici avec l'API configurée.
                getAllComptes(useJson); // Passer le booléen au lieu de l'objet ApiService
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Optional: Handle case when nothing is selected
            }
        });

        // Configuration du RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialisation de l'adaptateur
        compteAdapter = new CompteAdapter(new ArrayList<>());
        recyclerView.setAdapter(compteAdapter);

        compteAdapter.setOnDeleteClickListener(position -> {
            Compte compte = compteAdapter.getComptes().get(position);
            deleteCompte(compte.getId(), position);
        });

        compteAdapter.setOnEditClickListener(compte -> {
            // Stocke l'ID du compte à modifier
            compteIdToUpdate = compte.getId();

            // Affiche le formulaire
            formCreateCompte.setVisibility(View.VISIBLE);

            // Remplit les champs avec les valeurs actuelles
            soldeInput.setText(String.valueOf(compte.getSolde()));
            typeInput.setSelection(typeAdapter.getPosition(compte.getType()));

            // Change le texte du bouton
            btnCreate.setText("Modifier Compte");
        });

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
        btnCreate.setOnClickListener(v -> {
            if (compteIdToUpdate != null) {
                updateCompte(compteIdToUpdate);
            } else {
                createCompte();
            }
        });

        // Récupérer tous les comptes
        getAllComptes(true); // Par défaut, on utilise JSON
    }

    // Méthode pour afficher/masquer le formulaire de création de compte
    private void toggleForm() {
        if (formCreateCompte.getVisibility() == View.GONE) {
            resetForm();
            formCreateCompte.setVisibility(View.VISIBLE);
        } else {
            formCreateCompte.setVisibility(View.GONE);
        }
    }

    private void updateCompte(Long id) {
        String solde = soldeInput.getText().toString();
        String type = typeInput.getSelectedItem().toString();

        if (solde.isEmpty()) {
            Toast.makeText(this, "Le solde est obligatoire", Toast.LENGTH_SHORT).show();
            return;
        }

        Compte updatedCompte = new Compte();
        updatedCompte.setSolde(Double.parseDouble(solde));
        updatedCompte.setType(type);

        Call<Compte> call = RetrofitClient.getApi(true).updateCompte(id, updatedCompte);
        call.enqueue(new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Compte modifié avec succès", Toast.LENGTH_SHORT).show();
                    // Réinitialiser le formulaire
                    resetForm();
                    // Rafraîchir la liste
                    getAllComptes(true);
                } else {
                    Toast.makeText(MainActivity.this, "Erreur lors de la modification du compte", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetForm() {
        compteIdToUpdate = null;
        soldeInput.setText("");
        typeInput.setSelection(0);
        btnCreate.setText("Créer un compte");
        formCreateCompte.setVisibility(View.GONE);
    }

    private void deleteCompte(Long id, int position) {
        Call<Void> call = RetrofitClient.getApi(true).deleteCompte(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Compte supprimé avec succès", Toast.LENGTH_SHORT).show();
                    compteAdapter.removeCompte(position);
                } else {
                    Toast.makeText(MainActivity.this, "Échec de la suppression du compte", Toast.LENGTH_SHORT).show();
                    Log.e("DeleteCompte", "Erreur: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("DeleteCompte", "Erreur: ", t);
            }
        });
    }

    // Méthode pour récupérer tous les comptes
    private void getAllComptes(boolean useJson) {
        ApiService apiService = RetrofitClient.getApi(useJson);
        Call<List<Compte>> call = apiService.getAllComptes();
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

        // Créer un objet compte
        Compte newCompte = new Compte();
        newCompte.setSolde(Double.parseDouble(solde));
        newCompte.setType(type);

        // Appeler l'API pour créer le compte
        Call<Compte> call = RetrofitClient.getApi(true).createCompte(newCompte);
        call.enqueue(new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Compte créé avec succès", Toast.LENGTH_SHORT).show();
                    resetForm();  // Réinitialiser le formulaire
                    getAllComptes(true);  // Rafraîchir la liste des comptes
                } else {
                    Toast.makeText(MainActivity.this, "Erreur lors de la création du compte", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
