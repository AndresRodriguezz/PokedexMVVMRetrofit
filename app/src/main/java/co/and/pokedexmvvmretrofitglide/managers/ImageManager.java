package co.and.pokedexmvvmretrofitglide.managers;

import android.app.Application;
import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

public class ImageManager {
    private static volatile ImageManager instance;

    private Application application;

    private ImageManager() {

    }

    public static ImageManager getInstance() {
        if(instance == null) {
            synchronized (ImageManager.class) {
                if(instance == null) {
                    instance = new ImageManager();
                }
            }
        }

        return instance;
    }

    public void initialize(@NonNull Application application) {
        this.application = application;
    }

    public Context getContext() {
        return application.getApplicationContext();
    }

    public void setImage(String url, ImageView imageView) {
        Glide.with(application.getApplicationContext())
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public void setImage(File file, ImageView imageView) {
        Glide.with(application.getApplicationContext())
                .load(file)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}