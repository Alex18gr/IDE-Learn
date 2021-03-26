package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import gr.alexc.idelearn.ui.classanalysis.parser.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ParameterRequirement {
    private String name;
    private TypeRequirement type;
    
    

    public ParameterRequirement() {
		super();
	}



	public ParameterRequirement(String name, TypeRequirement type) {
		super();
		this.name = name;
		this.type = type;
	}



	public boolean checkParameter(Parameter parameter) {
        if (parameter.getName().equals(name)) {
            return TypeRequirement.compareTypeRequirementWithType(type, parameter.getType());
        } else {
            return false;
        }
    }



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public TypeRequirement getType() {
		return type;
	}



	public void setType(TypeRequirement type) {
		this.type = type;
	}
	
	
}
