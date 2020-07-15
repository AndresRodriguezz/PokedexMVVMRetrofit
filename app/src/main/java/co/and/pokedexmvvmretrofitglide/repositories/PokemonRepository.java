package co.and.pokedexmvvmretrofitglide.repositories;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import co.and.pokedexmvvmretrofitglide.Utils;
import co.and.pokedexmvvmretrofitglide.interfaces.HomeView;
import co.and.pokedexmvvmretrofitglide.models.DataPokemon;
import co.and.pokedexmvvmretrofitglide.models.Pokemon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonRepository {

    private HomeView view;
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
        view.showLoading();
        Call<DataPokemon> pokemonCall = Utils.getApi().getListPokemons();

        pokemonCall.enqueue(new Callback<DataPokemon>() {
            @Override
            public void onResponse(Call<DataPokemon> call, Response<DataPokemon> response) {
                view.hideLoding();
                if(response.isSuccessful() && response.body() != null){
                    view.setPokemons(response.body().getResults());
                    pokemonList.setValue(response.body().getResults());
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
