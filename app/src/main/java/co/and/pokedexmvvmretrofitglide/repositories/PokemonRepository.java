package co.and.pokedexmvvmretrofitglide.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import co.and.pokedexmvvmretrofitglide.Utils;
import co.and.pokedexmvvmretrofitglide.models.DataPokemon;
import co.and.pokedexmvvmretrofitglide.models.Pokemon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonRepository {
    private static volatile PokemonRepository instance;
    private static final String TAG = "POKEDEX";

    private final MutableLiveData<List<Pokemon>> pokemonList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> visibility = new MutableLiveData<>();
    private final MutableLiveData<Boolean> readyToCharge = new MutableLiveData<>();

    private int data = 0;

    private PokemonRepository() {
        List<Pokemon> pokemonList = new ArrayList<>();
        this.pokemonList.setValue(pokemonList);

        this.readyToCharge.setValue(true);
        this.visibility.setValue(false);

    }

    public static PokemonRepository getInstance() {
        if (instance == null) {
            synchronized (PokemonRepository.class) {
                if (instance == null) {
                    instance = new PokemonRepository();
                }
            }
        }

        return instance;
    }

    public void setPokemonList(List<Pokemon> list) {
        this.pokemonList.setValue(list);
    }

    public void setVisibility(boolean visibility) {
        this.visibility.setValue(visibility);
    }

    public void setReadyToCharge(boolean readyToCharge) {
        this.readyToCharge.setValue(readyToCharge);
    }


    @NonNull
    public LiveData<List<Pokemon>> getPokemonList() {
        return pokemonList;
    }

    @NonNull
    public LiveData<Boolean> getVisibility() {
        return visibility;
    }

    @NonNull
    public LiveData<Boolean> getReadyToCharge() {
        return readyToCharge;
    }


    public void loadPokemons() {
        Call<DataPokemon> pokemonCall = Utils.getApi().getListPokemons(data, 20);
        pokemonCall.enqueue(new Callback<DataPokemon>() {
            @Override
            public void onResponse(@NonNull Call<DataPokemon> call, @NonNull Response<DataPokemon> response) {
                setReadyToCharge(true);

                if (response.isSuccessful() && response.body() != null) {
                    DataPokemon dataPokemon = response.body();
                    List<Pokemon> results = dataPokemon.getResults();

                    for (Pokemon pokemon : results) {
                        Log.i(TAG, "Pokemon: " + pokemon.getName());
                    }
                    setPokemonList(results);
                } else {
                    Log.i("errorCallBack", "error");
                }

                setVisibility(true);
            }

            @Override
            public void onFailure(@NonNull Call<DataPokemon> call, @NonNull Throwable throwable) {
                setReadyToCharge(true);
                Log.i("error onFailure", "error");
                setVisibility(true);

            }
        });
    }

    public void addData() {
        data += 20;
    }
}