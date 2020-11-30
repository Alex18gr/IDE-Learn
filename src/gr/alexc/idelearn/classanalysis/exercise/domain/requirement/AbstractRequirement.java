package gr.alexc.idelearn.classanalysis.exercise.domain.requirement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import gr.alexc.idelearn.classanalysis.parser.ClassEntity;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClassRequirement.class, name = "class")
})
public abstract class AbstractRequirement {

    public abstract String getDescription();

    public abstract boolean checkRequirement(ClassEntity classEntity);
    
    public abstract Integer getSubrequirements();
}
