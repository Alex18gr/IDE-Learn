package gr.alexc.idelearn.classanalysis.exercise.domain.requirement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import gr.alexc.idelearn.classanalysis.parser.ClassEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(scope=ClassRequirement.class, generator= ObjectIdGenerators.PropertyGenerator.class, property="class_id")
public class ClassRequirement extends AbstractRequirement {

    @JsonProperty("class_id")
    private Integer classId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("related_requirements")
    private List<AbstractSubRequirement> relatedRequirements;


    @Override
    public String getDescription() {
        return "Must exist a class with the name \"" + name + "\".";
    }

    @Override
    public boolean checkRequirement(ClassEntity classEntity) {
        return classEntity.getClassName().equals(name);
    }

	@Override
	public Integer getTotalSubRequirements() {
		return relatedRequirements.size();
	}

	@Override
	public List<AbstractSubRequirement> getSubRequirements() {
		return relatedRequirements;
	}
}
