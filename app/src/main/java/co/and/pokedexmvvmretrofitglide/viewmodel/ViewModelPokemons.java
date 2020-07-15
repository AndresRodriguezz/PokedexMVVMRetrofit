package co.and.pokedexmvvmretrofitglide.viewmodel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import co.and.pokedexmvvmretrofitglide.repositories.PokemonRepository;
import co.and.pokedexmvvmretrofitglide.interfaces.HomeView;
import co.and.pokedexmvvmretrofitglide.models.Pokemon;

public class ViewModelPokemons extends ViewModel {
    private HomeView homeView;
    private PokemonRepository repositoryPokemon;
    private MediatorLiveData<List<Pokemon>> allPokemons;

    public ViewModelPokemons(){
        this.repositoryPokemon = PokemonRepository.getInstance();
    }

    public LiveData<List<Pokemon>> getAllPokemons() {
        repositoryPokemon.getPokemons();
        allPokemons = repositoryPokemon.getPokemonList();
        return allPokemons;
    }

}
