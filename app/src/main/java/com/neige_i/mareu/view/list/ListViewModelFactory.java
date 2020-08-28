package com.neige_i.mareu.view.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.neige_i.mareu.data.DI;

public class ListViewModelFactory implements ViewModelProvider.Factory {

    private static ListViewModelFactory factory;

    public static ListViewModelFactory getInstance() {
        return factory = factory == null ? new ListViewModelFactory() : factory;
    }

    private ListViewModelFactory() {
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListViewModel.class)) {
            return (T) new ListViewModel(DI.getRepository()/*new MeetingRepositoryImpl()*/);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
