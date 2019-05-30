package br.edu.ifc.videira.crud.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.provider.LiveFolders;

import java.util.List;

import br.edu.ifc.videira.crud.entities.User;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("DELETE FROM users")
    void deleteAll();

    @Query("SELECT * FROM users ORDER BY email ASC")
    LiveData<List<User>> getUsers();
}
