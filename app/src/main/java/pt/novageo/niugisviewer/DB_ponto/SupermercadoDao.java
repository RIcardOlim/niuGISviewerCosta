package pt.novageo.niugisviewer.DB_ponto;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

import pt.novageo.niugisviewer.Tabela.Supermercado;

/**
 * Created by ricardo on 10-11-2017.
 *
 */

 @Dao
public interface SupermercadoDao {

    @Query("SELECT * FROM Supermercado")
    List<Supermercado> getAll();

    @Query("SELECT * FROM Supermercado " +
            "WHERE nome_ponto LIKE :nomePonto")
    Supermercado findDatabyNome(String nomePonto);

    @Query("SELECT * FROM Supermercado WHERE _id LIKE :id")
    Supermercado findDatabyID(int id);

    @Insert
    void insert(Supermercado... supermercado);

    @Delete
    void delete(Supermercado supermercado);

    @Update
    void update(Supermercado supermercado);
 }
