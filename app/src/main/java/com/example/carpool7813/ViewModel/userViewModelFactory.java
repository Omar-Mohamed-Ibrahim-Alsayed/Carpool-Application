package com.example.carpool7813.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class userViewModelFactory implements ViewModelProvider.Factory {
        private final Application application;
        public userViewModelFactory(Application myApplication) {
            application = myApplication;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new userViewModel(application);
        }

}
