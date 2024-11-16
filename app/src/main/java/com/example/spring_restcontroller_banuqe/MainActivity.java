package com.example.spring_restcontroller_banuqe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
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

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private CompteAdapter compteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation du RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        // Configuration du RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        compteAdapter = new CompteAdapter(new ArrayList<>());
        recyclerView.setAdapter(compteAdapter);

        // Charger les comptes depuis l'API
        getAllComptes();
    }

    // Méthode pour récupérer tous les comptes
    private void getAllComptes() {
        // Appel à l'API pour récupérer la liste des comptes
        Call<List<Compte>> call = RetrofitClient.getApi().getAllComptes();
        call.enqueue(new Callback<List<Compte>>() {
            @Override
            public void onResponse(Call<List<Compte>> call, Response<List<Compte>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Mise à jour de l'adapter avec les comptes récupérés
                    List<Compte> comptes = response.body();
                    compteAdapter.setComptes(comptes);
                    Log.d(TAG, "Comptes fetched successfully: " + comptes);
                } else {
                    // Si l'API retourne une erreur
                    Log.e(TAG, "Error fetching comptes: " + response.code());
                    Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Compte>> call, Throwable t) {
                // Si l'appel API échoue
                Log.e(TAG, "Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to connect to the API", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
