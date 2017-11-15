package pt.novageo.niugisviewer.Api.response;

/**
 * Created by rica
 * rdo on 14-11-2017.
 */

public abstract class BaseResponse {

    protected int status;
    protected String response;

    public String getMessage(){return response == null || response.isEmpty() ? "test" : response;}

    public boolean isError() {return status == 0;}
}
