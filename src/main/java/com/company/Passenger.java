package com.company;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

/**
 * A Passenger has a name and a preferred seat type (Window, Aisle, etc.) and is assigned a seat on the flight.
 * 
 * @author AC75096046
 *
 */
@PlanningEntity
public class Passenger {
    @PlanningId
    public String name;

    @PlanningVariable(valueRangeProviderRefs = "seatRange")
    public Seat seat;

    public Seat.Type preferredSeatType;
}
