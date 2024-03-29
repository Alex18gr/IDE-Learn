package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
	    @JsonSubTypes.Type(value = ExtendClassSubRequirement.class, name = "extend"),
	    @JsonSubTypes.Type(value = ExtendNameSubRequirement.class, name = "extend-name"),
	    @JsonSubTypes.Type(value = ImplementNameSubRequirement.class, name = "implement-name"),
        @JsonSubTypes.Type(value = ContainsSubRequirement.class, name = "contains"),
        @JsonSubTypes.Type(value = ClassHasFieldRequirement.class, name = "contains-field"),
        @JsonSubTypes.Type(value = ClassHasMethodRequirement.class, name = "method"),
        @JsonSubTypes.Type(value = ClassHasConstructorRequirement.class, name = "constructor"),
        @JsonSubTypes.Type(value = ClassOverridesObjectMethod.class, name = "override-object-method"),
        @JsonSubTypes.Type(value = MethodCallInsideMethod.class, name = "method-call-method"),
        @JsonSubTypes.Type(value = MethodCallInsideConstructor.class, name = "constructor-call-method"),
        @JsonSubTypes.Type(value = SuperConstructorCallInsideConstructor.class, name = "constructor-call-constructor")
})
public abstract class AbstractSubRequirement implements Requirement {
	
	public Integer getSubrequirements() {
		return 0;
	}
	
}
