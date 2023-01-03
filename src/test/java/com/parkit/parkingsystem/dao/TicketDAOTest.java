package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;


public class TicketDAOTest {
	
	private static TicketDAO ticketDAO;
	private static Ticket ticket;
	
	@BeforeAll
	static void initTest() {
		
		ticketDAO = new TicketDAO();
		ticket = new Ticket();

	}
	
	@Test
	public void saveTicketTest() throws Exception {
		
		
		String reg = "ABCDEF";
		Date inTime = new Date();
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		double price = 0.0;

		ticket.setPrice(price);
		ticket.setVehicleRegNumber(reg);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		boolean bool = ticketDAO.saveTicket(ticket);
		assertFalse(bool);
	}
	
	@Test
	public void getTicketTest() {
		
		
		Ticket tickettest = ticketDAO.getTicket(ticket.getVehicleRegNumber());
		//assertEquals(tickettest.getVehicleRegNumber(), ticket.getVehicleRegNumber());
		assertTrue(ticket.equals(tickettest));
	}
	
	
	@Test
	public void updateTicketTest() throws Exception {
		
		
		Ticket ticket2 = ticketDAO.getTicket(ticket.getVehicleRegNumber());
		ticket2.setPrice(3.0);
		ticketDAO.updateTicket(ticket2);
		Ticket ticket3 = ticketDAO.getTicket(ticket2.getVehicleRegNumber());
		assertEquals(3.0, ticket3.getPrice());
		
	}
	

}
