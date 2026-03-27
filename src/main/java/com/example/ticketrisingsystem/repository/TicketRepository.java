package com.example.ticketrisingsystem.repository;

import com.example.ticketrisingsystem.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	// Add custom queries if needed
}
