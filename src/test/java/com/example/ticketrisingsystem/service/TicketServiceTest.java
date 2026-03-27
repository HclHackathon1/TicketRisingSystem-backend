package com.example.ticketrisingsystem.service;

import com.example.ticketrisingsystem.entity.Ticket;
import com.example.ticketrisingsystem.entity.TicketStatus;
import com.example.ticketrisingsystem.repository.TicketStatusTransitionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TicketServiceTest {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketStatusTransitionRepository transitionRepository;

    @Test
    void raiseTicket_shouldCreateOpenTicket() {
        Ticket ticket = ticketService.raiseTicket("Login issue", "Unable to login", "user");

        assertNotNull(ticket.getId());
        assertEquals(TicketStatus.OPEN, ticket.getStatus());
        assertEquals("user", ticket.getCreatedBy());
        assertEquals(1, transitionRepository.findByTicketIdOrderByChangedAtAsc(ticket.getId()).size());
    }

    @Test
    void assignTicket_shouldAllowAdminOnly() {
        Ticket ticket = ticketService.raiseTicket("Issue", "Description", "user");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> ticketService.assignTicket(ticket.getId(), "admin", "user", false));
        assertTrue(ex.getReason().contains("Only admin"));

        Ticket assigned = ticketService.assignTicket(ticket.getId(), "admin", "admin", true);
        assertEquals(TicketStatus.ASSIGNED, assigned.getStatus());
        assertEquals("admin", assigned.getAssignedTo());
    }

    @Test
    void closeTicket_shouldFailWithoutResolutionNote() {
        Ticket ticket = ticketService.raiseTicket("Issue", "Description", "user");
        ticketService.assignTicket(ticket.getId(), "admin", "admin", true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> ticketService.closeTicket(ticket.getId(), "admin"));
        assertTrue(ex.getReason().contains("closed only from RESOLVED")
                || ex.getReason().contains("without resolution note"));
    }

    @Test
    void transitions_shouldBeTracked() {
        Ticket ticket = ticketService.raiseTicket("Issue", "Description", "user");
        ticketService.assignTicket(ticket.getId(), "admin", "admin", true);
        ticketService.resolveTicket(ticket.getId(), "Fix applied", "admin");
        ticketService.closeTicket(ticket.getId(), "admin");

        var transitions = transitionRepository.findByTicketIdOrderByChangedAtAsc(ticket.getId());
        assertEquals(4, transitions.size());
        assertNull(transitions.get(0).getFromStatus());
        assertEquals(TicketStatus.OPEN, transitions.get(0).getToStatus());
        assertEquals(TicketStatus.ASSIGNED, transitions.get(1).getToStatus());
        assertEquals(TicketStatus.RESOLVED, transitions.get(2).getToStatus());
        assertEquals(TicketStatus.CLOSED, transitions.get(3).getToStatus());
    }
}
