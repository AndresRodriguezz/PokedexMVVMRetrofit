package co.and.pokedexmvvmretrofitglide;

import android.app.Application;

import co.and.pokedexmvvmretrofitglide.managers.ImageManager;

public class AppPokemons extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ImageManager.getInstance().initialize(this);
    }
}