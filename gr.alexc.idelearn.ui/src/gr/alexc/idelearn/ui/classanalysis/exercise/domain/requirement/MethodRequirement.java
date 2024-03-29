package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gr.alexc.idelearn.ui.classanalysis.parser.Method;
import gr.alexc.idelearn.ui.classanalysis.parser.Modifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MethodRequirement {

    private String name;
    private List<String> modifiers = Collections.emptyList();
    private TypeRequirement type;
    private List<ParameterRequirement> parameters = Collections.emptyList();

    public MethodRequirement(String name, List<String> modifiers, TypeRequirement type,
			List<ParameterRequirement> parameters) {
		super();
		this.name = name;
		this.modifiers = modifiers;
		this.type = type;
		this.parameters = parameters;
	}

	public MethodRequirement() {
		super();
	}

	public List<String> getParameterTypeList() {
        List<String> paramStrings = new ArrayList<>();
        for (ParameterRequirement p : parameters) {
            paramStrings.add(p.getType().toString());
        }
        Collections.sort(paramStrings);
        return paramStrings;
    }

    public boolean checkMethod(Method method) {
        // check the name
        if (method.getName().equals(name)) {
            // check the modifiers
            for (Modifier modifier : method.getModifiers()) {
                boolean found = false;
                for (String m : modifiers) {
                    if (modifier.getName().equals(m)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            }

            // check the type
            if (!TypeRequirement.compareTypeRequirementWithType(type, method.getType())) {
                return false;
            }

            // check the parameters
            List<String> checkingMethodParameterTypeList = method.getParameterTypeList();
            List<String> methodParameterTypeList = getParameterTypeList();
            if (checkingMethodParameterTypeList.size() == methodParameterTypeList.size()) {
                for (int i = 0; i < checkingMethodParameterTypeList.size(); i++) {
                    if (!checkingMethodParameterTypeList.get(i).equals(methodParameterTypeList.get(i))) {
                        return false;
                    }
                }
            } else {
                // wrong number of parameters
                return false;
            }

//            for (Parameter p : method.getParameters()) {
//                boolean found = false;
//                for (ParameterRequirement parameterRequirement : parameters) {
//                    if (parameterRequirement.checkParameter(p)) {
//                        found = true;
//                    }
//                }
//                if (!found) {
//                    return false;
//                }
//            }

            // when everything is true
            return true;
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

	public List<String> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

	public TypeRequirement getType() {
		return type;
	}

	public void setType(TypeRequirement type) {
		this.type = type;
	}

	public List<ParameterRequirement> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParameterRequirement> parameters) {
		this.parameters = parameters;
	}
    
    
}
