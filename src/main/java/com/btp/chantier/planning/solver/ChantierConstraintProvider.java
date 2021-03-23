package com.btp.chantier.planning.solver;

import org.optaplanner.core.api.score.stream.Constraint;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.sum;

import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;
import org.optaplanner.examples.cloudbalancing.domain.CloudProcess;
import org.optaplanner.examples.taskassigning.domain.Task;


/**
 * We can use the Constraint Stream API to create the constraints of our application in Java.
 * 
 * We’ll have these constraints:
 * 
 * Hard:
 * 	 - One employee can work only in one chantier at a given time
 *   - a chantier must have been provided into requested skills
 *   - 
 * 
 * Soft:
 * 	- Minimiser le delai des travaux
 * 
 * To do this, we create a ConstraintProvider that implements these constraints:
 * 
 * check examples from
 * 
 * C:\Marc\softs\optaplanner-distribution-8.2.0.Final\examples\sources\src\main\java\org\optaplanner\examples\
 * 
 * * cloudbalancing\optional\score\CloudBalancingConstraintProvider.java
 * * taskassigning\optional\score\TaskAssigningConstraintProvider.java

 * 
 * @author AC75096046
 *
 */
public class ChantierConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
            onlyOnePassengerPerSeat(constraintFactory),
            seatTypePreference(constraintFactory)
        };
    }
    
    // ************************************************************************
    // Hard constraints
    // ************************************************************************

    Constraint requiredCpuPowerTotal(ConstraintFactory constraintFactory) {
        return constraintFactory.from(CloudProcess.class)
                .groupBy(CloudProcess::getComputer, sum(CloudProcess::getRequiredCpuPower))
                .filter((computer, requiredCpuPower) -> requiredCpuPower > computer.getCpuPower())
                .penalize("requiredCpuPowerTotal",
                        HardSoftScore.ONE_HARD,
                        (computer, requiredCpuPower) -> requiredCpuPower - computer.getCpuPower());
    }
    
    private Constraint noMissingSkills(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Task.class)
                .filter(task -> task.getMissingSkillCount() > 0)
                .penalize("No missing skills",
                        BendableScore.ofHard(BENDABLE_SCORE_HARD_LEVELS_SIZE, BENDABLE_SCORE_SOFT_LEVELS_SIZE, 0, 1),
                        Task::getMissingSkillCount);
    }

    private Constraint onlyOnePassengerPerSeat(ConstraintFactory constraintFactory) {
        return constraintFactory.fromUniquePair(Passenger.class, Joiners.equal(p -> p.seat))
                   .penalize("Only one passenger per seat", HardSoftScore.ONE_HARD);
    }

    // ************************************************************************
    // Soft constraints
    // ************************************************************************
    
    private Constraint seatTypePreference(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Passenger.class)
                   .join(Seat.class, Joiners.equal(p -> p.seat, s -> s))
                   .filter((p,s) -> p.preferredSeatType != null && p.preferredSeatType != s.type)
                   .penalize("Seat type preference", HardSoftScore.ONE_SOFT);
    }

}
