package com.neige_i.mareu.view.add;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.neige_i.mareu.data.DI;

public class AddViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    @Nullable
    private static AddViewModelFactory factory;
    @NonNull
    private final Application application;

//    /**
//     * Creates a {@code AndroidViewModelFactory}
//     *
//     * @param application an application to pass in {@link androidx.lifecycle.AndroidViewModel}
//     */
//    public AddViewModelFactory(@NonNull Application application) {
//        super(application);
//    }

    @NonNull
    public static AddViewModelFactory getInstance(@NonNull Application application) {
        if (factory == null) {
            synchronized (AddViewModelFactory.class) {
                if (factory == null)
                    factory = new AddViewModelFactory(application);
            }
        }
        return factory;
    }

    private AddViewModelFactory(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddViewModel.class)) {
            return (T) new AddViewModel(DI.getMeetingRepository(), DI.getClock(), application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
