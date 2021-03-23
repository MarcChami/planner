package com.company;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@PlanningSolution
public class Flight {

    public String id;

    @PlanningScore
    public HardSoftScore score;

    @PlanningEntityCollectionProperty
    public List<Passenger> passengerList;

    public int rowCount;
    public int columnCount;

    @ValueRangeProvider(id = "seatRange")
    private List<Seat> calculateSeatList() {
        List<Seat> out = new ArrayList<>(rowCount * columnCount);
        final int MIDDLE_OF_ROW = columnCount / 2;
        for (int column = 0; column < columnCount; column++) {
            for (int row = 0; row < rowCount; row++) {
                Seat seat = new Seat();
                // 'A' + n = nth letter of the alphabet
                seat.seatIdentifier = (row + 1) + String.valueOf((char)('A' + column));
                seat.type = (column == 0 || column == columnCount - 1)? Seat.Type.WINDOW :
                    (column == MIDDLE_OF_ROW || column == MIDDLE_OF_ROW + 1)? Seat.Type.AISLE :
                        Seat.Type.OTHER;
                out.add(seat);
            }
        }
        return out;
    }
}
