package com.parkit.parkingsystem;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;
    private static InputReaderUtil inputReaderUtil;
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    
    @BeforeEach
    	public void setUp() {
    
    parkingSpotDAO = mock(ParkingSpotDAO.class);
    inputReaderUtil = mock(InputReaderUtil.class);
    ticketDAO = mock(TicketDAO.class);
    parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    
    }
    
    @Test
    public void processExitingVehicleTest(){
    	 try {
             when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
             
             ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
             Ticket ticket = new Ticket();
             ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
             ticket.setParkingSpot(parkingSpot);
             ticket.setVehicleRegNumber("ABCDEF");
             when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
             when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

             when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
         } catch (Exception e) {
             e.printStackTrace();
             throw  new RuntimeException("Failed to set up test mock objects");
         }
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }
    
	
	@Test 
	public void getNextParkingNumberIfAvailableTest() {
		when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
		when(inputReaderUtil.readSelection()).thenReturn(1);
		ParkingSpot parking = parkingService.getNextParkingNumberIfAvailable();
		assertEquals(parking.getId(), 1);
		assertEquals(parking.getParkingType(), ParkingType.CAR);
		assertTrue(parking.isAvailable());
	}
	 
	
	@Test
	public void processIncomingVehicleTest() {
		try {
			when(inputReaderUtil.readSelection()).thenReturn(1);
			when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		} catch (Exception e) {
			e.printStackTrace();
		}
		parkingService.processIncomingVehicle();
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
		verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
	}

}
