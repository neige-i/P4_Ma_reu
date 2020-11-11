package com.neige_i.mareu.view.list.view_model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.neige_i.mareu.data.DI;

public class ListViewModelFactory implements ViewModelProvider.Factory {

    // -------------------------------------  CLASS VARIABLES --------------------------------------

    @Nullable
    private static ListViewModelFactory factory;

    // ---------------------------------------- CONSTRUCTOR ----------------------------------------

    private ListViewModelFactory() {
    }

    // -------------------------------------- FACTORY METHODS --------------------------------------

    @NonNull
    public static ListViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ListViewModelFactory.class) {
                if (factory == null)
                    factory = new ListViewModelFactory();
            }
        }
        return factory;
    }

    // -------------------------------- VIEW MODEL FACTORY METHODS ---------------------------------

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListViewModel.class)) {
            return (T) new ListViewModel(DI.getListingRepository(), DI.getClock());
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
