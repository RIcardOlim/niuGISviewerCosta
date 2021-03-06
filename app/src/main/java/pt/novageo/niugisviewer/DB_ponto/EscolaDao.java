package pt.novageo.niugisviewer.DB_ponto;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

import pt.novageo.niugisviewer.Tabela.Escola;

/**
 * Dao
 * Created by ricardo on 09-11-2017.
 */

@Dao
public interface EscolaDao {

    @Query("SELECT * FROM Escola")
    List<Escola> getAll();

    @Query("SELECT * FROM Escola " +
            "WHERE nome_ponto LIKE :nomePonto")
    Escola findDatabyNome(String nomePonto);

    @Query("SELECT * FROM Escola WHERE _id LIKE :id")
    Escola findDatabyID(int id);

    @Insert
    void insert(Escola... escola);

    @Delete
    void delete(Escola escola);

    @Update
    void update(Escola escola);
}
