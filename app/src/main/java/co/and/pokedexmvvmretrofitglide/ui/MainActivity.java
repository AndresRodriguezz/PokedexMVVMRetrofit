package co.and.pokedexmvvmretrofitglide.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import co.and.pokedexmvvmretrofitglide.interfaces.HomeView;
import co.and.pokedexmvvmretrofitglide.R;
import co.and.pokedexmvvmretrofitglide.adapters.RecyclerPokemonsItems;
import co.and.pokedexmvvmretrofitglide.models.Pokemon;
import co.and.pokedexmvvmretrofitglide.viewmodel.ViewModelPokemons;
import retrofit2.Retrofit;

import android.os.Bundle;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity implements HomeView {
    private Retrofit retrofit;

    public static final String TAG = "POKEDEX";
    private RecyclerView recyclerView;
    private RecyclerPokemonsItems adapterPokemons;
    private ViewModelPokemons viewmodelPokemons;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerCategory);
        viewmodelPokemons = new ViewModelProvider(this).get(ViewModelPokemons.class);
        hideLoding();

    }

    @Override
    public void showLoading() {
        findViewById(R.id.shimmerPokemon).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoding() {
        findViewById(R.id.shimmerPokemon).setVisibility(View.GONE);

    }

    @Override
    public void setPokemons(List<Pokemon> pokemons) {
        hideLoding();
        adapterPokemons = new RecyclerPokemonsItems(pokemons, this);
        recyclerView.setAdapter(adapterPokemons);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(true);

        viewmodelPokemons.getAllPokemons().observe(this, new Observer<List<Pokemon>>() {
            @Override
            public void onChanged(List<Pokemon> pokemons) {
                adapterPokemons.setDataPokemons(pokemons);
            }
        });


    }


}