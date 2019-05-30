package br.edu.ifc.videira.crud.view_models;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import br.edu.ifc.videira.crud.entities.User;
import br.edu.ifc.videira.crud.repositories.UserRepository;

public class UserViewModel extends AndroidViewModel {

        private UserRepository mRepository;

        private LiveData<List<User>> mAllUsers;

        public UserViewModel (Application application) {
            super(application);
            mRepository = new UserRepository(application);
            mAllUsers = mRepository.getAllUsers();
        }

        public LiveData<List<User>> getAllUsers() { return mAllUsers; }

        public void insert(User user) { mRepository.insert(user); }
}
