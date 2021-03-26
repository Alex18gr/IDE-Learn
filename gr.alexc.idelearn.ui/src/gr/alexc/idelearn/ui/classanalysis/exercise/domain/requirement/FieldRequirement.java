package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import java.util.List;

import gr.alexc.idelearn.ui.classanalysis.parser.Field;
import gr.alexc.idelearn.ui.classanalysis.parser.Modifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class FieldRequirement {
    private String name;
    private List<String> modifiers;
    private TypeRequirement type;

    public FieldRequirement() {
		super();
	}

	public FieldRequirement(String name, List<String> modifiers, TypeRequirement type) {
		super();
		this.name = name;
		this.modifiers = modifiers;
		this.type = type;
	}

	public boolean checkField(Field field) {
        // check the name
        if (field.getName().equals(name) || name.equals("")) {
            // check the modifiers
            for (Modifier modifier : field.getModifiers()) {
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
            if (!TypeRequirement.compareTypeRequirementWithType(type, field.getType())) {
                return false;
            }

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
    
    
}
