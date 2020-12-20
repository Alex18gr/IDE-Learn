package gr.alexc.idelearn.ui.classanalysis.exercise.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import gr.alexc.idelearn.ui.classanalysis.exercise.ExerciseCheckReport;
import gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement.AbstractRequirement;
import gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement.AbstractSubRequirement;
import gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement.ClassRequirement;
import gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement.Requirement;
import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Exercise {

	private String id;
	private String name;
	private List<String> targets;
	@JsonProperty("exercise_project_info")
	private ExerciseProjectInfo exerciseProjectInfo;
	private List<AbstractRequirement> requirements;

	@JsonIgnore
	private ExerciseCheckReport exerciseCheckReport;

	public Exercise() {
		exerciseCheckReport = new ExerciseCheckReport(this); // create an empty report
	}

	public String getRequirementsDescription() {
		StringBuilder builder = new StringBuilder();

		builder.append("Exercise ").append(name).append(System.lineSeparator()).append("Requirements:")
				.append(System.lineSeparator());

		requirements.forEach((r) -> {
			builder.append("\t").append(r.getDescription()).append(System.lineSeparator());
		});

		return builder.toString();
	}

	public void checkExercise(Map<String, ClassEntity> classEntityMap) {
		Set<String> classNames = classEntityMap.keySet();
		for (AbstractRequirement requirement : requirements) {
			if (requirement instanceof ClassRequirement) {
				Optional<ClassEntity> classEntityOptional = Optional
						.ofNullable(classEntityMap.get(((ClassRequirement) requirement).getName()));

				if (classEntityOptional.isPresent()) {
					System.out.println(((ClassRequirement) requirement).getName() + " -> "
							+ requirement.checkRequirement(classEntityOptional.get()));
					for (AbstractSubRequirement subRequirement : ((ClassRequirement) requirement)
							.getRelatedRequirements()) {
						System.out.println("\t - " + subRequirement.getDescription() + " -> "
								+ subRequirement.checkRequirement(classEntityOptional.get()));
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
			totalRequirements += req.getTotalSubRequirements();
		}
		return totalRequirements;
	}
	
	public List<Requirement> getAllRequirements() {
        List<Requirement> allRequirements = new ArrayList<>();
        for (AbstractRequirement requirement : requirements) {
            allRequirements.add(requirement);
            allRequirements.addAll(requirement.getSubRequirements());
        }
        return allRequirements;
    }

}
