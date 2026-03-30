package com.example.ticketrisingsystem.repository;

import com.example.ticketrisingsystem.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	List<Ticket> findByCreatedByOrderByCreatedAtDesc(String createdBy);
}
