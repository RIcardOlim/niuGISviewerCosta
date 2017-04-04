package pt.novageo.niugisviewer;

/**
 * Created by estagiario on 31/03/2017.
 *
 */

public class Locais {

    private int _id;
    private String nomelocal;
    private String descricao;

    public Locais(String nomelocal) {
        this.nomelocal = nomelocal;
    }

    public void set_descricao(String descricao) {
        this.descricao = descricao;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_nomelocal(String nomelocal) {
        this.nomelocal = nomelocal;
    }

    public String get_descricao() {
        return descricao;
    }

    public int get_id() {
        return _id;
    }

    public String get_nomelocal() {
        return nomelocal;
    }
}
