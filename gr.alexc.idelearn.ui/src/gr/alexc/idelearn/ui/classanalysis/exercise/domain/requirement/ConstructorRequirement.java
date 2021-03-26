package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gr.alexc.idelearn.ui.classanalysis.parser.ConstructorMethod;
import gr.alexc.idelearn.ui.classanalysis.parser.Modifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ConstructorRequirement {

    private List<String> modifiers;
    private List<ParameterRequirement> parameters;

    public List<String> getParameterTypeList() {
        List<String> paramStrings = new ArrayList<>();
        for (ParameterRequirement p : parameters) {
            paramStrings.add(p.getType().toString());
        }
        Collections.sort(paramStrings);
        return paramStrings;
    }

    public boolean checkMethod(ConstructorMethod method) {
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

        // when everything is true
        return true;
    }

	public List<String> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

	public List<ParameterRequirement> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParameterRequirement> parameters) {
		this.parameters = parameters;
	}
    
    
}
