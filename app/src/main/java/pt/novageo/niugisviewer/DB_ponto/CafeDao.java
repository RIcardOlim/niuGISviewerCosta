package pt.novageo.niugisviewer.DB_ponto;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import pt.novageo.niugisviewer.Tabela.Cafe;

/**
 * Created by ricardo on 1
 * 0-11-2017.
 */

@Dao
public interface CafeDao {

    @Query("SELECT * FROM Cafe")
    List<Cafe> getAll();

    @Query("SELECT * FROM Cafe " +
            "WHERE nome_ponto LIKE :nomePonto")
    Cafe findDatabyNome(String nomePonto);

    @Query("SELECT * FROM Cafe WHERE _id LIKE :id")
    Cafe findDatabyId(int id);

    @Insert
    void insert(Cafe... cafe);

    @Delete
    void delete(Cafe cafe);

    @Update
    void update(Cafe cafe);
}
