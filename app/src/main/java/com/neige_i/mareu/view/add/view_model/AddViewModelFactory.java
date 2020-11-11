package com.neige_i.mareu.view.add.view_model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.neige_i.mareu.data.DI;

public class AddViewModelFactory implements ViewModelProvider.Factory {

    // -------------------------------------  CLASS VARIABLES --------------------------------------

    @Nullable
    private static AddViewModelFactory factory;

    // ---------------------------------------- CONSTRUCTOR ----------------------------------------

    private AddViewModelFactory() {
    }

    // -------------------------------------- FACTORY METHODS --------------------------------------

    @NonNull
    public static AddViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (AddViewModelFactory.class) {
                if (factory == null)
                    factory = new AddViewModelFactory();
            }
        }
        return factory;
    }

    // -------------------------------- VIEW MODEL FACTORY METHODS ---------------------------------

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddViewModel.class)) {
            return (T) new AddViewModel(DI.getMeetingRepository(), DI.getClock());
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
