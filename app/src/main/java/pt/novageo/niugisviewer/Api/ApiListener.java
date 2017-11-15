package pt.novageo.niugisviewer.Api;

import io.reactivex.disposables.Disposable;

/**
 * Created by ricard
 * o on 14-11-2017.
 */

public interface ApiListener<url> {

    void subscribe(Disposable disposable);
    void onSuccess(String url);
    void onFailure(String error);
}
