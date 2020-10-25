package com.neige_i.mareu.view.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.neige_i.mareu.data.DI;

public class ListViewModelFactory implements ViewModelProvider.Factory {

    @Nullable
    private static ListViewModelFactory factory;

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

    private ListViewModelFactory() {
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListViewModel.class)) {
            return (T) new ListViewModel(DI.getRepository(), DI.getClock());
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
