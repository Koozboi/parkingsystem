package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class FareCalculatorServiceIT {
	
	private static FareCalculatorService fareCalculatorService;
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static Ticket ticket;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    static void setUp() throws Exception{
    	fareCalculatorService = new FareCalculatorService();
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
        ticket = new Ticket();

    }

    @BeforeEach
    void setUpPerTest() throws Exception {
    	ticket.setVehicleRegNumber("ABC");
        dataBasePrepareService.clearDataBaseEntries();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);

    }
    
    @Test
    void calculateFareTestCarLessThanThirtyMinutes() throws Exception {
    	
    	Date inTime = new Date();
    	inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000 ));
    	Date outTime = new Date();
    	outTime.setTime(System.currentTimeMillis());
    	ticket.setInTime(inTime);
    	ticket.setOutTime(outTime);
    	ticketDAO.saveTicket(ticket);
    	
    	fareCalculatorService.calculateFare(ticket);
    	assertEquals(0, ticket.getPrice());
    	
    }
    
    @Test
    void calculateFareTestBikeLessThanThirtyMinutes() throws Exception {
    	
    	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setParkingSpot(parkingSpot);
    	
    	Date inTime = new Date();
    	inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000 ));
    	Date outTime = new Date();
    	outTime.setTime(System.currentTimeMillis());
    	ticket.setInTime(inTime);
    	ticket.setOutTime(outTime);
    	ticketDAO.saveTicket(ticket);
    	
    	fareCalculatorService.calculateFare(ticket);
    	assertEquals(0, ticket.getPrice());
    	
    }
    
    @Test
    void calculateFareTestCarMoreThanOneHour() throws Exception {

    	Date inTime = new Date();
    	inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000 ));
    	Date outTime = new Date();
    	outTime.setTime(System.currentTimeMillis());
    	ticket.setInTime(inTime);
    	ticket.setOutTime(outTime);
    	ticketDAO.saveTicket(ticket);
    	    	
    	fareCalculatorService.calculateFare(ticket);
    	assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }
    
    @Test
    void calculateFareTestBikeMoreThanOneHour() throws Exception {
    	
    	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setParkingSpot(parkingSpot);

    	Date inTime = new Date();
    	inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000 ));
    	Date outTime = new Date();
    	outTime.setTime(System.currentTimeMillis());
    	ticket.setInTime(inTime);
    	ticket.setOutTime(outTime);
    	ticketDAO.saveTicket(ticket);
    	    	
    	fareCalculatorService.calculateFare(ticket);
    	assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
    }
    
    @Test
    void calculateFareTestForARecurringUser() throws Exception {
    	Ticket ticket = new Ticket();
    	
    	Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticketDAO.saveTicket(ticket);
        fareCalculatorService.calculateFare(ticket);
        
        Ticket ticket2 = new Ticket();

        ticket2.setInTime(inTime);
        ticket2.setOutTime(outTime);
        ticket2.setParkingSpot(parkingSpot);
        ticket2.setVehicleRegNumber("ABCDEF");
        ticketDAO.saveTicket(ticket2);
        fareCalculatorService.calculateFare(ticket2);
        
        
        assertEquals(1.42, ticket2.getPrice());
    }
}
