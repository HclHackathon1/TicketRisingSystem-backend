package com.example.ticketrisingsystem.repository;

import com.example.ticketrisingsystem.entity.TicketStatusTransition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketStatusTransitionRepository extends JpaRepository<TicketStatusTransition, Long> {
    List<TicketStatusTransition> findByTicketIdOrderByChangedAtAsc(Long ticketId);

    void deleteByTicketId(Long ticketId);
}
