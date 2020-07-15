package co.and.pokedexmvvmretrofitglide.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.and.pokedexmvvmretrofitglide.R;
import co.and.pokedexmvvmretrofitglide.adapters.PokemonAdapter;
import co.and.pokedexmvvmretrofitglide.models.Pokemon;
import co.and.pokedexmvvmretrofitglide.viewmodel.ViewModelPokemons;

public class MainActivity extends AppCompatActivity {
    private LinearLayout llShimmer;

    private PokemonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        llShimmer = findViewById(R.id.llShimmer);

        adapter = new PokemonAdapter(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setAdapter(adapter);

        ViewModelPokemons viewModel = new ViewModelProvider(this).get(ViewModelPokemons.class);

        viewModel.getPokemons().observe(this, new Observer<List<Pokemon>>() {
            @Override
            public void onChanged(List<Pokemon> list) {
                adapter.setList(list);
            }
        });

        viewModel.getVisibility().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean visibility) {
                if(visibility) {
                    if(llShimmer.getVisibility() != View.GONE) {
                        llShimmer.setVisibility(View.GONE);
                    }
                } else {
                    if(llShimmer.getVisibility() != View.VISIBLE) {
                        llShimmer.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        viewModel.loadPokemons();
    }
}