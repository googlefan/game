package com.zipeiyi.game.data.handler;

import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;

import java.text.ParseException;

public interface GameHandler {
    void execute(MessageReq messageReq, MessageRes messageRes) throws ParseException;
}
