package me.j360.shiro.appclient.web;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class Response implements Serializable {

    @Getter
    @Setter
    private int status = 0;

    @Getter
    @Setter
    private String error = "";

    public Response(){

    }
    public Response(int status,String error){
        this.status = status;
        this.error = error;
    }
}
