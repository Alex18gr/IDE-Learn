package gr.alexc.idelearn.classanalysis.exercise;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import gr.alexc.idelearn.classanalysis.exercise.domain.Exercise;
import gr.alexc.idelearn.classanalysis.exercise.domain.requirement.AbstractRequirement;
import gr.alexc.idelearn.classanalysis.exercise.domain.requirement.Requirement;

public class ExerciseCheckReport {

	private Exercise exercise;
	private Float completedPercentage;
	private Integer completedRequirements;
    private final Map<Requirement, Boolean> report;

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

    public void updateRequirementStatus(Requirement requirement, Boolean status) {
        report.put(requirement, status);
        calculateCompletedPercentage();
    }

    public Boolean getRequirementStatus(Requirement requirement) {
        Boolean requirementStatus = report.get(requirement);
        if (requirementStatus == null) return false;
        return requirementStatus;
    }

    public Set<Requirement> getAllAnalysedRequirements() {
        return report.keySet();
    }
    
    private void calculateCompletedPercentage() {
    	Set<Requirement> requirementsSet = report.keySet();
    	int completedRequirements = 0;
    	for (Requirement req : requirementsSet) {
    		if (report.get(req)) { // if the requirement status is true (completed)
    			completedRequirements++;
    		}
    	}
    	this.completedRequirements = completedRequirements;
    	this.completedPercentage = (float) ((((float) completedRequirements) / (float) exercise.getTotalRequirements()) * 100.0);
    }
}
