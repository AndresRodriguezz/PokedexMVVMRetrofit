package co.and.pokedexmvvmretrofitglide.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import co.and.pokedexmvvmretrofitglide.models.Pokemon;
import co.and.pokedexmvvmretrofitglide.repositories.PokemonRepository;

public class ViewModelPokemons extends ViewModel {
    private final PokemonRepository repository;
    private LiveData<List<Pokemon>> pokemons;
    private LiveData<Boolean> visibility;
    private LiveData<Boolean> readyToCharge;


    public ViewModelPokemons() {
        this.repository = PokemonRepository.getInstance();
        this.pokemons = repository.getPokemonList();
        this.visibility = repository.getVisibility();
        this.readyToCharge = repository.getReadyToCharge();

    }

    @NonNull
    public LiveData<List<Pokemon>> getPokemons() {
        return pokemons;
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
        repository.loadPokemons();
    }

    public void addOffset() {
        repository.addData();

    }

}
