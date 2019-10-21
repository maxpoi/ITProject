package com.example.homesweethome.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.homesweethome.AppRepository;
import com.example.homesweethome.AppDataBase.Entities.User;
import com.example.homesweethome.HelperClasses.HomeSweetHome;

public class UserViewModel extends AndroidViewModel {

    private LiveData<User> user;
    private LiveData<String> protraitImagePath;
    private AppRepository appRepository;

    public UserViewModel(@NonNull Application application, AppRepository appRepository, String userEmail) {
        super(application);
        this.appRepository = appRepository;
        user = appRepository.getUser();
        protraitImagePath = appRepository.getUserPortraitImagePath(userEmail);
    }



    // use factory to create this view model as Room only call the no parameter constructor.
    public static class UserViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application application;
        private final String userEmail;
        private final AppRepository appRepository;

        public UserViewModelFactory(@NonNull Application application, String userEmail) {
            this.application = application;
            this.userEmail = userEmail;
            this.appRepository = ((HomeSweetHome)application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new UserViewModel(application, appRepository, userEmail);
        }
    }

    public LiveData<User> getUser() { return user; }
    public LiveData<String> getProtraitImagePath() { return protraitImagePath; }

    public void addUser(User user) { appRepository.addUser(user); }
    public void deleteUser(User user) { appRepository.deleteUser(user); }

    public User getStaticUser() { return appRepository.getStaticUser(); }
}
