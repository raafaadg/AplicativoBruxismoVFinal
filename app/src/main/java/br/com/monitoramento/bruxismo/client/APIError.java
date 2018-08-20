package br.com.monitoramento.bruxismo.client;

/**
 * Created by Cesar on 29/03/2017.
 */
public class APIError {

    private int statusCode;
    private String message;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}
