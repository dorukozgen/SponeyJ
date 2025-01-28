package com.dorukozgen.sponeyj.websocket;

import com.dorukozgen.sponeyj.MainApplication;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class WebSocketEventListener implements ApplicationListener<ContextRefreshedEvent> {
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        MainApplication.wsConnected = true;
    }
}
