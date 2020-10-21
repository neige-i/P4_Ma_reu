package com.neige_i.mareu.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.neige_i.mareu.data.DI;
import com.neige_i.mareu.view.list.ListViewModel;

public class BaseViewModelFactory implements ViewModelProvider.Factory {

    private static BaseViewModelFactory factory;

    public static BaseViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (BaseViewModelFactory.class) {
                if (factory == null)
                    factory = new BaseViewModelFactory();
            }
        }
        return factory;
    }

    private BaseViewModelFactory() {
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BaseViewModel.class)) {
            return (T) new BaseViewModel(DI.getRepository());
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
