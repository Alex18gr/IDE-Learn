package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClassRequirement.class, name = "class")
})
public abstract class AbstractRequirement implements Requirement {
	
	public abstract List<AbstractSubRequirement> getSubRequirements();

	public abstract Integer getTotalSubRequirements();
	
}
