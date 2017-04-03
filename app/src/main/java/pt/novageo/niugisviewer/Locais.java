package pt.novageo.niugisviewer;

/**
 * Created by estagiario on 31/03/2017.
 *
 */

public class Locais {

    private int _id;
    private String _nomelocal;

    public Locais() {
    }

    public Locais(String nomelocal) {
        this._nomelocal = nomelocal;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_nomelocal(String _nomelocal) {
        this._nomelocal = _nomelocal;
    }

    public int get_id() {
        return _id;
    }

    public String get_nomelocal() {
        return _nomelocal;
    }
}
