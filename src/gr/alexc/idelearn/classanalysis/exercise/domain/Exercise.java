package gr.alexc.idelearn.classanalysis.exercise.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import gr.alexc.idelearn.classanalysis.exercise.domain.requirement.AbstractRequirement;
import gr.alexc.idelearn.classanalysis.exercise.domain.requirement.AbstractSubRequirement;
import gr.alexc.idelearn.classanalysis.exercise.domain.requirement.ClassRequirement;
import gr.alexc.idelearn.classanalysis.parser.ClassEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Exercise {

    private String id;
    private String name;
    private List<String> targets;
    private List<AbstractRequirement> requirements;

    public String getRequirementsDescription() {
        StringBuilder builder = new StringBuilder();

        builder.append("Exercise ").append(name)
            .append(System.lineSeparator()).append("Requirements:").append(System.lineSeparator());

        requirements.forEach((r) -> {
            builder.append("\t").append(r.getDescription()).append(System.lineSeparator());
        });

        return builder.toString();
    }

    public void checkExercise(Map<String, ClassEntity> classEntityMap) {
        Set<String> classNames = classEntityMap.keySet();
        for (AbstractRequirement requirement : requirements) {
            if (requirement instanceof ClassRequirement) {
                Optional<ClassEntity> classEntityOptional = Optional.ofNullable(classEntityMap.get(((ClassRequirement) requirement).getName()));

                if (classEntityOptional.isPresent()) {
                    System.out.println(((ClassRequirement) requirement).getName() + " -> " +
                            requirement.checkRequirement(classEntityOptional.get()));
                    for (AbstractSubRequirement subRequirement : ((ClassRequirement) requirement).getRelatedRequirements()) {
                        System.out.println("\t - " + subRequirement.getDescription() + " -> " +
                                subRequirement.checkRequirement(classEntityOptional.get()));
                    }
                } else {
                    System.out.println(((ClassRequirement) requirement).getName() + " -> false");
                }
            }
        }
    }
    
    public Integer getTotalRequirements() {
    	int totalRequirements = 0;
    	for (AbstractRequirement req : requirements) {
    		totalRequirements++;
    		totalRequirements += req.getSubrequirements();
    	}
    	return totalRequirements;
    }

}
