package com.neige_i.mareu.view.add;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.neige_i.mareu.data.DI;

public class AddViewModelFactory implements ViewModelProvider.Factory {

    private static AddViewModelFactory factory;

    public static AddViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (AddViewModelFactory.class) {
                if (factory == null)
                    factory = new AddViewModelFactory();
            }
        }
        return factory;
    }

    private AddViewModelFactory() {
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddViewModel.class)) {
            return (T) new AddViewModel(DI.getRepository(), DI.getClock());
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
