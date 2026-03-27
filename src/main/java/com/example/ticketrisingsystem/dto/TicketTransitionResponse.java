package com.example.ticketrisingsystem.dto;

import com.example.ticketrisingsystem.entity.TicketStatus;
import com.example.ticketrisingsystem.entity.TicketStatusTransition;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class TicketTransitionResponse {
    TicketStatus fromStatus;
    TicketStatus toStatus;
    String changedBy;
    String note;
    Instant changedAt;

    public static TicketTransitionResponse fromEntity(TicketStatusTransition transition) {
        return TicketTransitionResponse.builder()
                .fromStatus(transition.getFromStatus())
                .toStatus(transition.getToStatus())
                .changedBy(transition.getChangedBy())
                .note(transition.getNote())
                .changedAt(transition.getChangedAt())
                .build();
    }
}
