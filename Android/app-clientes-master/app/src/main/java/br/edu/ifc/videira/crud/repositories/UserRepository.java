package br.edu.ifc.videira.crud.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import br.edu.ifc.videira.crud.dao.UserDao;
import br.edu.ifc.videira.crud.db.AppDatabase;
import br.edu.ifc.videira.crud.entities.User;

public class UserRepository {

        private UserDao userDao;
        private LiveData<List<User>> allUsers;

        public UserRepository(Application application) {
            AppDatabase db = AppDatabase.getDatabase(application);
            userDao = db.userDao();
            allUsers = userDao.getUsers();
        }

        public LiveData<List<User>> getAllUsers() {
            return allUsers;
        }

        public void insert (User user) {
            new insertAsyncTask(userDao).execute(user);
        }

        private static class insertAsyncTask extends AsyncTask<User, Void, Void> {

            private UserDao mAsyncTaskDao;

            insertAsyncTask(UserDao dao) {
                mAsyncTaskDao = dao;
            }

            @Override
            protected Void doInBackground(final User... params) {
                mAsyncTaskDao.insert(params[0]);
                return null;
            }
        }
}
