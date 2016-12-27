package me.j360.shiro.webclient.web;

public abstract class Request {

    public interface ParamFilter{
        public void filter();
    }

    public interface Validation{
        public void validation();
    }

    public void execute(ParamFilter paramFilter){
        paramFilter.filter();
    }

    public void execute(Validation validation){
        validation.validation();
    }

    public abstract Response checkArguments();

}
