package com.company;

import java.util.concurrent.ExecutionException;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.optaplanner.core.api.solver.SolverManager;

@ApplicationScoped
public class FlightService {
    
	
	private static final Logger LOGGER = Logger.getLogger(FlightService.class);
	
	@Inject
    SolverManager<Flight, String> solverManager;

    public void addPassengerToFlight(Flight flight, Passenger passenger) {
    	LOGGER.info("addPassengerToFlight: passenger name:" +passenger.name + " to flight id:" + flight.id);
        flight.passengerList.add(passenger);
    }

    public Flight createSeatAssignments(Flight flight) {
        try {
            return solverManager.solve(flight.id, flight).getFinalBestSolution();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}