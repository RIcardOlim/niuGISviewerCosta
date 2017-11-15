package pt.novageo.niugisviewer.Api.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ricardo on 14-11-
 * 2017.
 */

public class AddPontoRequest {

    @SerializedName("user")
//    private String user;
    private String service;
//    private String password;
    private String pontos;

    public AddPontoRequest(String service, /*String user, String password, */String pontos){

        this.service = service;
        /*this.user = user;
        this.password = password;*/
        this.pontos = pontos;
    }
}
