
# SOLVER

https://docs.optaplanner.org/6.2.0.Final/optaplanner-docs/html/ch04.html
https://www.optaplanner.org/learn/slides/optaplanner-presentation.pdf

https://www.optaplanner.org/blog/2016/10/26/DomainModelingGuide.html

https://docs.optaplanner.org/6.3.0.Final/optaplanner-docs/html/ch04.html

C:\Marc\softs\optaplanner-distribution-8.2.0.Final\examples\sources\src\main\java\org\optaplanner\examples\cloudbalancing\optional\score\CloudBalancingConstraintProvider.java
C:\Marc\softs\optaplanner-distribution-8.2.0.Final\examples\sources\src\main\java\org\optaplanner\examples\taskassigning\optional\score\TaskAssigningConstraintProvider.java


https://www.optaplanner.org/blog/2020/04/07/ConstraintStreams.html

rule "Don't assign Ann"
    when
        Shift(getEmployee().getName() == "Ann")
    then
        scoreHolder.addSoftConstraintMatch(kcontext, -1);
end

This is the same constraint in Java using Constraint Streams:

Constraint constraint = constraintFactory
        .from(Shift.class)
        .filter(shift -> shift.getEmployee().getName().equals("Ann"))
        .penalize("Don't assign Ann", HardSoftScore.ONE_SOFT);


Rq DRL: https://docs.jboss.org/drools/release/5.2.0.Final/drools-expert-docs/html/ch05.html#d0e2785

rule "name"
    attributes
    when
        LHS
    then
        RHS
end



[Modeling]:

* dessiner un schéma:
	- mettre les entités et virer les doublons etc
	- en orange =  ce qui va changer durant l'optimisation
	- en violet = les shadow variables = ca change pendant l'optim mais c'est déduit d'autres planing variables
* verifier les chained planning variables:
	- definir l'ordre d'enchainement des planing entities (vehicle routing)
* transformer les relations many to many en one to many et une many to one = creer class intermediaire
	- pas de @PlanningVariable sur des collection => pb de perf
	en fait la y a 2 many to one relation: ShiftAssignement a 1 Employee et a 1 Shift (1 créneau, 1 rotation)
* Il ne faut pas plusieurs planningVariable: par ex: il faut choisir pour shiftassignement soit le shift soit le employee => alors lequel des 2 ? => cf le dernier point
* Il faut que le business sache ce que ca signie qd le planning variable est null, pas seulement par l'id =>  ? (les null rajoute des espaces de recherche)
* Il faut pouvoir distinguer plusieurs planning entity (ils ont les memes pb properties), ex: dans Shift il faut rajouter indexInShift
* Choisir un model ou le nombre de planning entities est fixe dans durant la résolution:
	ex: le nb d'employé par créneau est connu et ne change pas, par contre le nb de creneau que va devoir avoir un employé = lui change selon la résolution !
	=> donc on va choisir que le Shift sera un problem property et que la relation avec l'employé sera une planning variable

	

ChantierConstraintProvider:

	définir les contraintes:

		HArd:
			* 1 personne est sur 1 seul chantier à 1 instant donné
			* le chantier doit être fournit en compétence requise
			
		Soft:
			* Eviter de réorganiser les équipes déjà attribuées à un chantier déjà en cours de réalisation (non disruptive solution)
			* affinité entre les employés
			* minimiser le délai des travaux (makespan)
			* prioriser les taches critiques
			* les employés préfèrent certaines tâches
			* faire monter en compétence les salariés sur des tâches
				
PlanningSolution = ce qu'on doit résoudre	(orga du vol)
   => 	Attribuer des ouvriers aux chantiers -> voir exemple des cpu et process
   
   - PlanningScore: hard et soft
   
   
chantier = computer
faire rentrer le staff dedans = process

Best Practices:

A problem fact class: used by the score constraints, but does NOT change during planning (For example in n queens, the columns and rows are problem facts)
A planning entity class: used by the score constraints and changes during planning.

Ask yourself: What class changes during planning? Which class has variables that I want the Solver to change for me?
That class is a planning entity.
Most use cases have only 1 planning entity class.
Most use cases also have only 1 planning variable per planning entity class.

design Good Model:

ManyToOne
In a many to one relationship, it's normally the many side that is the planning entity class.
The property referencing the other side is then the planning variable.
For example in employee rostering:
the planning entity class is ShiftAssignment (affectation), not Employee,
and the planning variable is ShiftAssignment.getEmployee() because 1 Employee has multiple ShiftAssignments but 1 ShiftAssignment has only 1 Employee.

1 chantier a plusieurs Ouvriers, mais 1 ouvrier est sur 1 chantier à la fois dans un temps donné
=> Employee = entity planning et la variable est sur quel chantier il va etre affecté !


 
-- exemple de l'avion   
FlightSeatingConstraintProvider


   - @PlanningEntityCollectionProperty = list de passagers = associé au vol

   - @ValueRangeProvider(id = "seatRange")
    private List<Seat> calculateSeatList() {
	
	
@PlanningEntity
public class Passenger {
 A Passenger has a name and a preferred seat type (Window, Aisle, etc.) and is assigned a seat on the flight.

    @PlanningId
    public String name;
	
	@PlanningVariable(valueRangeProviderRefs = "seatRange")
    public Seat seat;

@PlanningId
public String seatIdentifier;



---- 


exemple avec computer: cpu, ram, bandwith, cost
process: requiredCPU, requiredMemory, requiredBandwith


-- 

other:

@PlanningPin = sert pour les repeated planning
A pinned planning entity is never changed during planning.For example, it allows the user to pin a shift to a specific employee before solvingand the solver will not undo that, regardless of the constraints. 
The boolean is false if the planning entity is movable and true if the planning entity is pinned

shadow variable:
https://docs.optaplanner.org/6.3.0.Final/optaplanner-docs/html/ch04.html#shadowVariable
//en gros si on change la route du vehicule, il faut decaler les heures d'arrivée chez les autres clients = système de listener de ces shadow variables

If there are multiple relationships (or fields): check for shadow variables. A shadow variable does change during planning, but its value can be calculated based on one or more genuine planning variables, without dispute. Color those shadow relationships (or fields) purple.
Only one side of a bi-directional relationship can be a genuine planning variable, the other side will become an inverse relation shadow variable later on. Keep those relationships in orange.

chained variable= order of planning entities ?
https://docs.optaplanner.org/6.1.0.Beta2/optaplanner-docs/html/plannerConfiguration.html

   * A chain is never a loop. The tail is always open.
   * Every chain always has exactly 1 anchor. The anchor is a problem fact, never a planning entity.
   * A chain is never a tree, it is always a line. Every anchor or planning entity has at most 1 trailing planning entity.
   * Every initialized planning entity is part of a chain.
   * An anchor with no planning entities pointing to it, is also considered a chain.

Planning variable:
For example: in task assignment with too many tasks for the workforce, we would rather leave low priority tasks unassigned instead of assigning them to an overloaded worker.
To allow an initialized planning variable to be null, set nullable to true:
@PlanningVariable(..., nullable = true)

ValueRange:
@ValueRangeProvider annotation always has a property id, which is referenced by the @PlanningVariable's property valueRangeProviderRefs.

This annotation can be located on 2 types of methods:
 * On the Solution: All planning entities share the same value range.
 * On the planning entity: The value range differs per planning entity. This is less common.

The return type of that method can be 2 types:
 * Collection: The value range is defined by a Collection (usually a List) of it's possible values.
 * ValueRange: The value range is defined by its bounds. This is less common.

Optimisation des algos:
  * Planning entity difficulty:  have an estimation of which planning entities are more difficult to plan
 difficultyComparatorClass to the @PlanningEntity annotation

  * Planning value strength:  have an estimation of which planning values are stronger, which means they are more likely to satisfy a planning entity
 a strengthComparatorClass to the @PlanningVariable annotation
 in bin packing bigger containers are more likely to fit an item and in course scheduling bigger rooms are less likely to break the student capacity constraint

Repeated Planning:
qd ca change: des backup (on laisse 1 employé de dispo pour absorber les pbds ?), le continuous planning (par windows de temps) et le temps reel = change soudain

Types de score:https://docs.optaplanner.org/latest/optaplanner-docs/html_single/index.html#calculateTheScore
* SimpleScore = 1 seul score
* HArdSoftScore = juste 1 hard et 1 soft
* HardMediumSoft = 1 hard et en fait 2 softs
* BendableScore = https://docs.optaplanner.org/latest/optaplanner-docs/html_single/index.html#bendableScore
  qd il y a plusieurs hard et soft score
  Par exemple 1 bendable de 1 hard et 2 soft = HardMediumSoft
* on peut en faire des customs

Faireness:
https://docs.optaplanner.org/latest/optaplanner-docs/html_single/index.html#fairnessScoreConstraints
* Fairly distribute the workload amongst the employees, to avoid envy.
* Evenly distribute the workload amongst assets, to improve reliability.

=> il y a une notion de calculer les carrés pour que ca soit plus equitable

Exemples:

https://docs.optaplanner.org/latest/optaplanner-docs/html_single/index.html
et chercher task assigning

public class CloudBalancingHelloWorld {

    public static void main(String[] args) {
        // Build the Solver
        SolverFactory<CloudBalance> solverFactory = SolverFactory.create(new SolverConfig()
                .withSolutionClass(CloudBalance.class)
                .withEntityClasses(CloudProcess.class)
                .withConstraintProviderClass(CloudBalancingConstraintProvider.class)
                .withTerminationSpentLimit(Duration.ofMinutes(2)));
        Solver<CloudBalance> solver = solverFactory.buildSolver();

        // Load a problem with 400 computers and 1200 processes
        CloudBalance unsolvedCloudBalance = new CloudBalancingGenerator().createCloudBalance(400, 1200);

        // Solve the problem
        CloudBalance solvedCloudBalance = solver.solve(unsolvedCloudBalance);

        // Display the result
        System.out.println("\nSolved cloudBalance with 400 computers and 1200 processes:\n"
                + toDisplayString(solvedCloudBalance));
    }
    
    ....
}


Task Assigning:
https://docs.optaplanner.org/latest/optaplanner-docs/html_single/index.html#taskAssigning

Project Scheduling:
https://docs.optaplanner.org/latest/optaplanner-docs/html_single/index.html#projectJobScheduling


# MCD 

npm install -g generator-jhipster
jhipster jdl ./my-jdl-file.jdl --json-only
jhipster jdl C:\Marc\projects\optimiser\src\main\resources\com\chantier\jhipster-jdl.jdl
