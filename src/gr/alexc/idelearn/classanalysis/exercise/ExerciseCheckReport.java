package gr.alexc.idelearn.classanalysis.exercise;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import gr.alexc.idelearn.classanalysis.exercise.domain.requirement.AbstractRequirement;

public class ExerciseCheckReport {

    private final Map<AbstractRequirement, Boolean> report;

    public ExerciseCheckReport() {
        report = new HashMap<>();
    }

    public void updateRequirementStatus(AbstractRequirement requirement, Boolean status) {
        report.put(requirement, status);
    }

    public Boolean getRequirementStatus(AbstractRequirement requirement) {
        return report.get(requirement);
    }

    public Set<AbstractRequirement> getAllAnalysedRequirements() {
        return report.keySet();
    }
}
