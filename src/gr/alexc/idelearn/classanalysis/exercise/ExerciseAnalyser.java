package gr.alexc.idelearn.classanalysis.exercise;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import gr.alexc.idelearn.classanalysis.exercise.domain.Exercise;
import gr.alexc.idelearn.classanalysis.exercise.domain.requirement.AbstractRequirement;
import gr.alexc.idelearn.classanalysis.exercise.domain.requirement.AbstractSubRequirement;
import gr.alexc.idelearn.classanalysis.exercise.domain.requirement.ClassRequirement;
import gr.alexc.idelearn.classanalysis.parser.ClassChecker;
import gr.alexc.idelearn.classanalysis.parser.ClassEntity;

public class ExerciseAnalyser {

    public ExerciseAnalyser() {
    }

    public ExerciseCheckReport analyseExerciseRequirements(Exercise exercise, ClassChecker checker) {

        ExerciseCheckReport report = exercise.getExerciseCheckReport();
        Map<String, ClassEntity> classEntityMap = checker.getVisitedClasses();
        List<AbstractRequirement> requirements = exercise.getRequirements();

        for (AbstractRequirement requirement : requirements) {
            if (requirement instanceof ClassRequirement) {
                Optional<ClassEntity> classEntityOptional = Optional.ofNullable(classEntityMap.get(((ClassRequirement) requirement).getName()));
                if (classEntityOptional.isPresent()) {
                    report.updateRequirementStatus(requirement, requirement.checkRequirement(classEntityOptional.get()));
                    for (AbstractSubRequirement subRequirement : ((ClassRequirement) requirement).getRelatedRequirements()) {
                        report.updateRequirementStatus(subRequirement, subRequirement.checkRequirement(classEntityOptional.get()));
                    }
                } else {
                    report.updateRequirementStatus(requirement, false);
                }
            }
        }
        return report;
    }

}
