package pt.novageo.niugisviewer.Tabela;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import pt.novageo.niugisviewer.DB_ponto.Converters;

/**
 * Created by ricardo on 10-11-2017.
 *
 */

@Entity(tableName = "Supermercado")
@TypeConverters({Converters.class})
public class Supermercado {

    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name = "nome_ponto")
    private String nomePonto;

    @ColumnInfo(name = "descrição_ponto")
    private String descPonto;

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

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setNomePonto(String nomePonto) {
        this.nomePonto = nomePonto;
    }

    public void setDescPonto(String descPonto) {
        this.descPonto = descPonto;
    }

    public void setTipoPonto(String tipoPonto) {
        this.tipoPonto = tipoPonto;
    }

    public void setLatPonto(double latPonto) {
        this.latPonto = latPonto;
    }

    public void setLngPonto(double lngPonto) {
        this.lngPonto = lngPonto;
    }

    public void setDataPonto(String dataPonto) {
        this.dataPonto = dataPonto;
    }

    public void setMoradaPonto(String moradaPonto) {
        this.moradaPonto = moradaPonto;
    }

    public void setImagemPonto(byte[] imagemPonto) {
        this.imagemPonto = imagemPonto;
    }

    public int get_id() {
        return _id;
    }

    public String getNomePonto() {
        return nomePonto;
    }

    public String getDescPonto() {
        return descPonto;
    }

    public String getTipoPonto() {
        return tipoPonto;
    }

    public double getLatPonto() {
        return latPonto;
    }

    public double getLngPonto() {
        return lngPonto;
    }

    public String getDataPonto() {
        return dataPonto;
    }

    public String getMoradaPonto() {
        return moradaPonto;
    }

    public byte[] getImagemPonto() {
        return imagemPonto;
    }
}
