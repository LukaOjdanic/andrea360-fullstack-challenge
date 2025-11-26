package com.andrea.fitness.websocket;

import com.andrea.fitness.dto.realTimeDto.SeatUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ReservationUpdateController {

    private final SimpMessagingTemplate template;

    @Autowired
    public ReservationUpdateController(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendUpdate(SeatUpdateDto seatUpdate) {
        template.convertAndSend("/topic/reservations", seatUpdate);
    }
}
