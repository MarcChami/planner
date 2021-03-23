package com.company;

import org.optaplanner.core.api.domain.lookup.PlanningId;

/**
 * A seat has a seat identifier (ex: "3A") and what type it is.
 * 
 * @author AC75096046
 *
 */
public class Seat {
    @PlanningId
    public String seatIdentifier;
    public Type type;

    public enum Type {
        WINDOW, AISLE, OTHER
    }
}
