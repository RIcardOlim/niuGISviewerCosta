package pt.novageo.niugisviewer.Tabela;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import pt.novageo.niugisviewer.DB_ponto.Converters;

/**
 * Created by ricardo on 09-11-2017.
 * ENTIDADE
 */

@Entity(tableName = "Escola")
@TypeConverters({Converters.class})
public class Escola {

    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name = "nome_ponto")
    private String nomePonto;

    @ColumnInfo(name = "descricao_ponto")
    private String descricaoPonto;

    @ColumnInfo(name = "tipo_ponto")
    private String tipoPonto;

    @ColumnInfo(name = "lat_ponto")
    private double latPonto;

    @ColumnInfo(name = "lng_ponto")
    private double lngPonto;

    @ColumnInfo(name = "data_ponto")
    private String dataPonto;

    @ColumnInfo(name = "morada_ponto")
    private String moradaPonto;

    @ColumnInfo(name = "imagem_ponto")
    private byte[] imagemPonto;

    public int get_id() {

        return _id;
    }

    public void set_id(int id){

        this._id = id;
    }

    public String getNomePonto(){

        return nomePonto;
    }

    public void setNomePonto(String nomePonto) {

        this.nomePonto = nomePonto;
    }

    public String getDescricaoPonto(){

        return descricaoPonto;
    }

    public void setDescricaoPonto(String descricaoPonto) {

        this.descricaoPonto = descricaoPonto;
    }

    public String getTipoPonto(){

        return tipoPonto;
    }

    public void setTipoPonto(String tipoPonto) {

        this.tipoPonto = tipoPonto;
    }

    public double getLatPonto(){

        return latPonto;
    }

    public void setLatPonto(double latPonto) {

        this.latPonto = latPonto;
    }

    public double getLngPonto(){

        return lngPonto;
    }

    public void setLngPonto(double lngPonto) {

        this.lngPonto = lngPonto;
    }

    public String getDataPonto(){

        return dataPonto;
    }

    public void setDataPonto(String dataPonto) {

        this.dataPonto = dataPonto;
    }

    public String getMoradaPonto(){

        return moradaPonto;
    }

    public void setMoradaPonto(String moradaPonto) {

        this.moradaPonto = moradaPonto;
    }

    public byte[] getImagemPonto(){

        return imagemPonto;
    }

    public void setImagemPonto(byte[] imagemPonto) {

        this.imagemPonto = imagemPonto;
    }
}
