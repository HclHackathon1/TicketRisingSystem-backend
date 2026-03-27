package com.example.ticketrisingsystem.service;

import com.example.ticketrisingsystem.entity.Ticket;
import com.example.ticketrisingsystem.entity.TicketStatus;
import com.example.ticketrisingsystem.entity.TicketStatusTransition;
import com.example.ticketrisingsystem.repository.TicketRepository;
import com.example.ticketrisingsystem.repository.TicketStatusTransitionRepository;
import com.example.ticketrisingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketStatusTransitionRepository transitionRepository;
    private final UserRepository userRepository;

    @Transactional
    public Ticket raiseTicket(String title, String description, String createdBy) {
        Ticket ticket = Ticket.builder()
                .title(title)
                .description(description)
                .status(TicketStatus.OPEN)
                .createdBy(createdBy)
                .build();

        Ticket saved = ticketRepository.save(ticket);
        recordTransition(saved, null, TicketStatus.OPEN, createdBy, "Ticket raised");
        return saved;
    }

    @Transactional
    public Ticket assignTicket(Long ticketId, String assignedTo, String changedBy, boolean isAdmin) {
        if (!isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admin can assign tickets");
        }

        Ticket ticket = getTicket(ticketId);
        if (ticket.getStatus() != TicketStatus.OPEN && ticket.getStatus() != TicketStatus.ASSIGNED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ticket can be assigned only when status is OPEN or ASSIGNED");
        }

        TicketStatus from = ticket.getStatus();
        var assignee = userRepository.findByUsername(assignedTo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assignee user not found"));
        ticket.setAssignedTo(assignee);
        ticket.setStatus(TicketStatus.ASSIGNED);
        Ticket saved = ticketRepository.save(ticket);
        recordTransition(saved, from, TicketStatus.ASSIGNED, changedBy, "Assigned to " + assignedTo);
        return saved;
    }

    @Transactional
    public Ticket resolveTicket(Long ticketId, String resolutionNote, String changedBy) {
        Ticket ticket = getTicket(ticketId);
        if (ticket.getStatus() != TicketStatus.ASSIGNED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ticket can be resolved only from ASSIGNED status");
        }

        TicketStatus from = ticket.getStatus();
        ticket.setResolutionNote(resolutionNote);
        ticket.setStatus(TicketStatus.RESOLVED);
        Ticket saved = ticketRepository.save(ticket);
        recordTransition(saved, from, TicketStatus.RESOLVED, changedBy, resolutionNote);
        return saved;
    }

    @Transactional
    public Ticket closeTicket(Long ticketId, String changedBy) {
        Ticket ticket = getTicket(ticketId);
        if (ticket.getStatus() != TicketStatus.RESOLVED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ticket can be closed only from RESOLVED status");
        }

        if (ticket.getResolutionNote() == null || ticket.getResolutionNote().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot close ticket without resolution note");
        }

        TicketStatus from = ticket.getStatus();
        ticket.setStatus(TicketStatus.CLOSED);
        Ticket saved = ticketRepository.save(ticket);
        recordTransition(saved, from, TicketStatus.CLOSED, changedBy, "Ticket closed");
        return saved;
    }

    @Transactional(readOnly = true)
    public Ticket getTicket(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
    }

    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<TicketStatusTransition> getTransitions(Long ticketId) {
        return transitionRepository.findByTicketIdOrderByChangedAtAsc(ticketId);
    }

    private void recordTransition(Ticket ticket, TicketStatus from, TicketStatus to, String changedBy, String note) {
        TicketStatusTransition transition = TicketStatusTransition.builder()
                .ticket(ticket)
                .fromStatus(from)
                .toStatus(to)
                .changedBy(changedBy)
                .note(note)
                .build();
        transitionRepository.save(transition);
    }
}
