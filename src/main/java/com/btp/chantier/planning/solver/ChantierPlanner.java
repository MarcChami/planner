package com.btp.chantier.planning.solver;

import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import com.btp.chantier.planning.model.Chantier;
import com.btp.chantier.planning.model.Employee;

@PlanningSolution
public class ChantierPlanner {

	public List<Chantier> chantierList;
	
	//on cherche a affecter des Employee a des chantiers
	public List<Employee> employeeList;

    @PlanningScore
    public HardSoftScore score;
	
	//pb fact: la liste des chantiers ne doit pas changer lors de la resolution du planning
	@ValueRangeProvider(id = "computerRange")
	@ProblemFactCollectionProperty
	public List<Chantier> getComputerList() {
		return chantierList;
	}
	
	//planning entity = on va dire a quel chantier doit etre affecté l'employé
	@PlanningEntityCollectionProperty
	public List<Employee> getEmployeeList() {
		return employeeList;
	}

}
