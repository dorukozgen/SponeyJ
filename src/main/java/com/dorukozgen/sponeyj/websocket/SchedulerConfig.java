package com.dorukozgen.sponeyj.websocket;

import com.dorukozgen.sponeyj.streamer.ThreadManager;
import com.dorukozgen.sponeyj.streamer.Values;
import com.dorukozgen.sponeyj.websocket.responses.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class SchedulerConfig {
    @Autowired
    SimpMessagingTemplate template;

    @Scheduled(fixedDelay = 300000L)
    public void sendAdhocMessages() {
        if (ThreadManager.isRunning())
            this.template.convertAndSend("/topic/update", new UpdateResponse(

                    Values.getThreadCount(),
                    Values.getTotalStreams(),
                    Values.getSuccessfulStreams(),
                    Values.getFailedStreams(),
                    Values.getEarning()));
    }

    @Scheduled(fixedDelay = 15000L)
    public void sendAdhocMessages2() {
        if (ThreadManager.isRunning())
            this.template.convertAndSend("/topic/update", new UpdateResponse(

                    Values.getThreadCount(),
                    Values.getTotalStreams(),
                    Values.getSuccessfulStreams(),
                    Values.getFailedStreams(),
                    Values.getEarning()));
    }
}
