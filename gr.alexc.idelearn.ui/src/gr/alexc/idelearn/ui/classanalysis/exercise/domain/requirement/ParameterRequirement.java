package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import gr.alexc.idelearn.ui.classanalysis.parser.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParameterRequirement {
    private String name;
    private TypeRequirement type;

    public boolean checkParameter(Parameter parameter) {
        if (parameter.getName().equals(name)) {
            return TypeRequirement.compareTypeRequirementWithType(type, parameter.getType());
        } else {
            return false;
        }
    }
}
