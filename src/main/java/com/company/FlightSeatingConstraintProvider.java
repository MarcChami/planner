package com.company;

import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;


/**
 * We can use the Constraint Stream API to create the constraints of our application in Java. We’ll have two constraints:
 * No two passengers can be in the same seat.
 * Maximize the number of passengers who get seats they prefer.
 * 
 * To do this, we create a ConstraintProvider that implements these constraints:
 * 
 * @author AC75096046
 *
 */
public class FlightSeatingConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
            onlyOnePassengerPerSeat(constraintFactory),
            seatTypePreference(constraintFactory)
        };
    }

    private Constraint onlyOnePassengerPerSeat(ConstraintFactory constraintFactory) {
        return constraintFactory.fromUniquePair(Passenger.class, Joiners.equal(p -> p.seat))
                   .penalize("Only one passenger per seat", HardSoftScore.ONE_HARD);
    }

    private Constraint seatTypePreference(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Passenger.class)
                   .join(Seat.class, Joiners.equal(p -> p.seat, s -> s))
                   .filter((p,s) -> p.preferredSeatType != null && p.preferredSeatType != s.type)
                   .penalize("Seat type preference", HardSoftScore.ONE_SOFT);
    }

}
