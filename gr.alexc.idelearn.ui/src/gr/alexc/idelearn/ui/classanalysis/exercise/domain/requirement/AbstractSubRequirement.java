package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ExtendSubRequirement.class, name = "extend"),
        @JsonSubTypes.Type(value = ContainsSubRequirement.class, name = "contains"),
        @JsonSubTypes.Type(value = ContainsSubRequirement.class, name = "contains"),
        @JsonSubTypes.Type(value = ClassHasFieldRequirement.class, name = "contains-field"),
        @JsonSubTypes.Type(value = ClassHasMethodRequirement.class, name = "method")
})
public abstract class AbstractSubRequirement implements Requirement {
	
	public Integer getSubrequirements() {
		return 0;
	}
	
}
