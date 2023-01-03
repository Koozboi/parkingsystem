package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.ZoneId;
import java.util.Date;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
	
	public FareCalculatorService() {
		
		ticketDAO = new TicketDAO(); 
	}
	
	private TicketDAO ticketDAO;
	private RecurringUser recurringUser = new RecurringUser();

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

		Date inTime = ticket.getInTime();
        Date outTime = ticket.getOutTime();

        Duration duration = Duration.between(inTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), outTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        
        switch (ticket.getParkingSpot().getParkingType()){
        
            case CAR: if (duration.toMinutes() > 30) {
            	if (!recurringUser.isRecurring(ticket.getVehicleRegNumber())) {
                ticket.setPrice((duration.toMinutes() * Fare.CAR_RATE_PER_HOUR) / 60);
            }
            	else {
            		ticket.setPrice(Math.floor((((duration.toMinutes() * Fare.CAR_RATE_PER_HOUR) / 60) * (0.95)) * 100) / 100);

            	}
            }
            else {
            	ticket.setPrice(0.0);
            }
            break;

            case BIKE: if (duration.toMinutes() > 30) {
            	if (!recurringUser.isRecurring(ticket.getVehicleRegNumber())) {
                ticket.setPrice((duration.toMinutes() * Fare.BIKE_RATE_PER_HOUR) / 60);
            	}
            	else {
            		ticket.setPrice(Math.floor((((duration.toMinutes() * Fare.BIKE_RATE_PER_HOUR) / 60) * (0.95)) * 100) / 100);

            	}
            }
            else {
            	ticket.setPrice(0.0);
            }
            break;
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
        }
    

	public TicketDAO getTicketDAO() {
		return ticketDAO;
	}

	public void setTicketDAO(TicketDAO ticketDAO) {
		this.ticketDAO = ticketDAO;
	}
}