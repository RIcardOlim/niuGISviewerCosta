package pt.novageo.niugisviewer.Api;

import android.content.Context;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pt.novageo.niugisviewer.Api.request.AddPontoRequest;
import pt.novageo.niugisviewer.Api.response.AddPontoResponse;

/**
 *
 * Created by ricardo on 14-11-2017.
 */

public class ApiManager {

    private static ApiManager sInstance;

    private Context mContext;

    public static ApiManager getInstance() {

        if(sInstance == null) {

            sInstance = new ApiManager();
        }

        return sInstance;
    }

    public void initialize(Context context) {
        mContext = context;
    }

    public void postAddPonto(final String service,
                             final String user,
                             final String password,
                             final String pontos,
                             final ApiListener<Void> apiListener) {

        AddPontoRequest addPontoRequest = new AddPontoRequest(service, pontos);
        ApiAdapter
                .getsInstance()
                .getApiInterface()
                .addPonto(addPontoRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<AddPontoResponse>(){

                    @Override
                    public void onSubscribe(Disposable d) {

                        apiListener.subscribe(d);
                    }

                    @Override
                    public void onNext(AddPontoResponse value) {

                        if(value.isError()) {

                            apiListener.onFailure(value.getMessage());
                            return;
                        }

                        apiListener.onSuccess(value.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiListener.onFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }
}
