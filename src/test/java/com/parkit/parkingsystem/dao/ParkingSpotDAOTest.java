package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;

public class ParkingSpotDAOTest {
	
	private static ParkingSpotDAO parkingDAO;
	private static DataBasePrepareService dataBasePrepareService;
	
	@BeforeAll
	public static void initTest() {
		parkingDAO = new ParkingSpotDAO();
		DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
		parkingDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
		dataBasePrepareService.clearDataBaseEntries();
	}
	
	@Test
	public void getNextAvailableSlotTest() {
		int result = parkingDAO.getNextAvailableSlot(ParkingType.CAR);
		assertNotNull(result);
	}
	
	@Test
	public void updateParkingTest() {
		
		// 1. récupérer un parking dispo
		
		int dispo = parkingDAO.getNextAvailableSlot(ParkingType.CAR);
		
		// 2. créer un objet parking et l'initialiser avec le parking number dispo de 1. et mettre à jour son available à "false"
		
		ParkingSpot parking = new ParkingSpot(dispo, ParkingType.CAR, false);
		
		// 3. utiliser updateParking en passant notre parking en entrée de la méthode
		
		parkingDAO.updateParking(parking);
		
		// 4. récupérer de nouveau un parking dispo
		
		int dispo2 = parkingDAO.getNextAvailableSlot(ParkingType.CAR);
		System.out.println(dispo);
		System.out.println(dispo2);
		// 5. test si parking dispo du 4. est différent de parking dispo de 1.
		
		assertNotEquals(dispo, dispo2);
	}
	
}
