package pt.novageo.niugisviewer.Api;

import io.reactivex.Observable;

import pt.novageo.niugisviewer.Api.request.AddPontoRequest;
import pt.novageo.niugisviewer.Api.response.AddPontoResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ricardo on 1
 * 4-11-2017.
 */

public interface ApiInterface {

    @POST("addPonto")
    Observable<AddPontoResponse> addPonto(@Body AddPontoRequest addPontoRequest);
}
