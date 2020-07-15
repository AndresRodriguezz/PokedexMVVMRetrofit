package co.and.pokedexmvvmretrofitglide.repositories;

import android.app.Application;
import android.content.Context;
import android.nfc.Tag;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import co.and.pokedexmvvmretrofitglide.Utils;
import co.and.pokedexmvvmretrofitglide.interfaces.HomeView;
import co.and.pokedexmvvmretrofitglide.models.DataPokemon;
import co.and.pokedexmvvmretrofitglide.models.Pokemon;
import co.and.pokedexmvvmretrofitglide.ui.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonRepository {

    private HomeView view;
    private static final String TAG = "POKEDEX";
    private static PokemonRepository instance;
    private MediatorLiveData<List<Pokemon>> pokemonList = new MediatorLiveData<>();

    public static PokemonRepository getInstance(){
        if(instance == null){
            instance = new PokemonRepository();
        }
        return instance;
    }

    public PokemonRepository() {
        List<Pokemon> pokemonList = new ArrayList<>();
        this.pokemonList.setValue(pokemonList);
    }

    public void  getPokemons() {
        //view.showLoading();
        Call<DataPokemon> pokemonCall = Utils.getApi().getListPokemons();

        pokemonCall.enqueue(new Callback<DataPokemon>() {
            @Override
            public void onResponse(Call<DataPokemon> call, Response<DataPokemon> response) {
                //view.hideLoding();
                if(response.isSuccessful() && response.body() != null){
                    DataPokemon dataPokemon = response.body();
                    List<Pokemon> pokemonList2 = dataPokemon.getResults();

                    for(int i = 0; i<pokemonList2.size();i++){
                        Pokemon p = pokemonList2.get(i);
                        Log.i(TAG,"Pokemon: "+p.getName());
                    }
                  //  view.setPokemons(response.body().getResults());
                    // pokemonList.setValue(response.body().getResults());
                } else{
                    Log.i("errorCallBack","error");
                }

            }

            @Override
            public void onFailure(Call<DataPokemon> call, Throwable t) {
                Log.i("error onFailure","error");
            }
        });
    }

    public MediatorLiveData<List<Pokemon>> getPokemonList() {
        return pokemonList;
    }
}
