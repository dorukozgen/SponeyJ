package com.dorukozgen.sponeyj.websocket.controllers;

import com.dorukozgen.sponeyj.streamer.Proxy;
import com.dorukozgen.sponeyj.streamer.Streamer;
import com.dorukozgen.sponeyj.streamer.ThreadManager;
import com.dorukozgen.sponeyj.streamer.Utils;
import com.dorukozgen.sponeyj.streamer.Values;
import com.dorukozgen.sponeyj.websocket.messages.StartMessage;
import com.dorukozgen.sponeyj.websocket.responses.StartResponse;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class StartController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping({"/start"})
    @SendTo({"/topic/start"})
    public StartResponse start(StartMessage message) throws Exception {
        System.out.println(Arrays.toString((Object[])message.getSongs()));
        try {
            if (ThreadManager.isRunning()) {
                ThreadManager.stopThreads();
                Values.reset();
                Utils.wait(1);
                return new StartResponse(true, "Stopped");
            }
            int threadCount = Integer.parseInt(message.getThreadCount());
            List<String> accounts = Utils.getTxtLines("accounts");
            List<String> proxies = Utils.getTxtLines("proxies");
            if (accounts.isEmpty() && message.getUseAccounts())
                return new StartResponse(false, "No accounts found.");
            if (proxies.isEmpty() && message.getUseProxy())
                return new StartResponse(false, "No proxies found.");
            if (message.getUseAccounts() && accounts.size() < threadCount)
                return new StartResponse(false, "Not enough accounts.");
            for (int i = 0; i < threadCount; i++) {
                Streamer streamer;
                String account = "";
                String username = "";
                String password = "";
                if (message.getUseAccounts()) {
                    account = accounts.get(i % accounts.size());
                    username = account.split(":")[0];
                    password = account.split(":")[1];
                }
                if (message.getUseProxy()) {
                    String proxyText = proxies.get(i % proxies.size());
                    String proxyHost = proxyText.split(":")[0];
                    int proxyPort = Integer.parseInt(proxyText.split(":")[1]);
                    String proxyUsername = proxyText.split(":")[2];
                    String proxyPassword = proxyText.split(":")[3];
                    if (message.getUseAccounts()) {
                        streamer = new Streamer(message.getSongs(), 8100 + i, i, username, password, new Proxy(proxyHost, proxyPort, proxyUsername, proxyPassword, message.getProxyType()));
                    } else {
                        streamer = new Streamer(message.getSongs(), 8100 + i, i, new Proxy(proxyHost, proxyPort, proxyUsername, proxyPassword, message.getProxyType()));
                    }
                } else if (message.getUseAccounts()) {
                    streamer = new Streamer(message.getSongs(), 8100 + i, i, username, password);
                } else {
                    streamer = new Streamer(message.getSongs(), 8100 + i, i);
                }
                ThreadManager.addThread(streamer);
            }
            ThreadManager.runThreads();
            this.simpMessagingTemplate.convertAndSend("/topic/update", message);
            return new StartResponse(true, "Started");
        } catch (Exception e) {
            System.out.println(e.getMessage() + " " + e.getMessage() + " " + e.getCause());
            return new StartResponse(false, e.getMessage());
        }
    }

    @MessageMapping({"/update"})
    public void update(StartMessage message) {
        System.out.println("update");
    }
}
