package com.zipeiyi.game.gateServer.exception;

/**
 * Created by zhangxiaoqiang on 16/12/2.
 */
public class ClientCloseException extends Exception{

    private static final String MESSAGE = "Can't close this client, beacuse the client didn't connect a server.";

    public ClientCloseException() {
        super(MESSAGE);
    }
}
