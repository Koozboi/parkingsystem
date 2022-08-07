package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.ZoneId;
import java.util.Date;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        ticket.getInTime();
		Date inTime = ticket.getInTime();
        Date outTime = ticket.getOutTime();

        Duration duration = Duration.between(inTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), outTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        
        //TODO: Some tests are failing here. Need to check if this logic is correct

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice((duration.toMinutes() * Fare.CAR_RATE_PER_HOUR) / 60);
                break;
            }
            case BIKE: {
                ticket.setPrice((duration.toMinutes() * Fare.BIKE_RATE_PER_HOUR) / 60);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");

        }
    }
}