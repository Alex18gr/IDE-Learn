package gr.alexc.idelearn.classanalysis.exercise;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import gr.alexc.idelearn.classanalysis.exercise.domain.Exercise;
import gr.alexc.idelearn.classanalysis.exercise.domain.requirement.AbstractRequirement;

public class ExerciseCheckReport {

	private Exercise exercise;
	private Float completedPercentage;
	private Integer completedRequirements;
    private final Map<AbstractRequirement, Boolean> report;

    public ExerciseCheckReport(Exercise exercise) {
        report = new HashMap<>();
        this.exercise = exercise;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public void updateRequirementStatus(AbstractRequirement requirement, Boolean status) {
        report.put(requirement, status);
        calculateCompletedPercentage();
    }

    public Boolean getRequirementStatus(AbstractRequirement requirement) {
        return report.get(requirement);
    }

    public Set<AbstractRequirement> getAllAnalysedRequirements() {
        return report.keySet();
    }
    
    private void calculateCompletedPercentage() {
    	Set<AbstractRequirement> requirementsSet = report.keySet();
    	int completedRequirements = 0;
    	for (AbstractRequirement req : requirementsSet) {
    		if (report.get(req)) { // if the requirement status is true (completed)
    			completedRequirements++;
    		}
    	}
    	this.completedRequirements = completedRequirements;
    	this.completedPercentage = (float) ((((float) completedRequirements) / (float) exercise.getTotalRequirements()) * 100.0);
    }
}
