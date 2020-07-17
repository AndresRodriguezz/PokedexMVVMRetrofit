package co.and.pokedexmvvmretrofitglide.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import co.and.pokedexmvvmretrofitglide.R;
import co.and.pokedexmvvmretrofitglide.adapters.MyAdapter;
import co.and.pokedexmvvmretrofitglide.models.Pokemon;
import co.and.pokedexmvvmretrofitglide.viewmodel.ViewModelPokemons;

public class MainActivity extends AppCompatActivity {
    private LinearLayout llShimmer;

   // private PokemonAdapter adapter;
    private MyAdapter myAdapter;

    private ViewModelPokemons viewModel;

    private Boolean readyToChargeActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        llShimmer = findViewById(R.id.llShimmer);

        viewModel = new ViewModelProvider(this).get(ViewModelPokemons.class);

       // adapter = new PokemonAdapter();
        myAdapter = new MyAdapter(this);

        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy > 0){
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if(readyToChargeActivity){
                        if((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            Log.i("POKEDEX", "Llegamos al final");
                            readyToChargeActivity = false;

                            viewModel.addOffset();
                            viewModel.loadPokemons();
                        }

                    }


                }
            }
        });


        viewModel.getPokemons().observe(this, new Observer<List<Pokemon>>() {
            @Override
            public void onChanged(List<Pokemon> list) {
                myAdapter.adicionarListaPokemon(list);
                //adapter.setList(list);
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

        viewModel.getReadyToCharge().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean readyToCharge) {
                if (readyToCharge) {
                    readyToChargeActivity = true;
                }
            }
        });
        viewModel.loadPokemons();

    }

}