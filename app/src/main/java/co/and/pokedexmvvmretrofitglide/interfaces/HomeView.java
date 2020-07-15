package co.and.pokedexmvvmretrofitglide.interfaces;

import java.util.List;

import co.and.pokedexmvvmretrofitglide.models.Pokemon;

public interface HomeView {
    void showLoading();
    void hideLoding();
    void setPokemons(List<Pokemon> pokemons);
}
