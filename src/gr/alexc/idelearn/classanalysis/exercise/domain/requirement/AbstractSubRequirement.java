package gr.alexc.idelearn.classanalysis.exercise.domain.requirement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ExtendSubRequirement.class, name = "extend"),
        @JsonSubTypes.Type(value = ContainsSubRequirement.class, name = "contains")
})
public abstract class AbstractSubRequirement extends AbstractRequirement {

}
