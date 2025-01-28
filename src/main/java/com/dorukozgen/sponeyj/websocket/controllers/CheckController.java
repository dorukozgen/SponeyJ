package com.dorukozgen.sponeyj.websocket.controllers;

import com.dorukozgen.sponeyj.streamer.License;
import com.dorukozgen.sponeyj.streamer.Utils;
import com.dorukozgen.sponeyj.websocket.messages.CheckMessage;
import com.dorukozgen.sponeyj.websocket.responses.CheckResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class CheckController {
    @MessageMapping({"/check"})
    @SendTo({"/topic/check"})
    public CheckResponse check(CheckMessage message) {
        if (License.getInstance().isVaild(message.getKey(), Utils.getHWID()))
            return new CheckResponse(true, "License is valid");
        return new CheckResponse(false, "License is not valid");
    }
}
