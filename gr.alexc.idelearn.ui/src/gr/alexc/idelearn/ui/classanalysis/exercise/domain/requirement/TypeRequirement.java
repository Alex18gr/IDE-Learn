package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import gr.alexc.idelearn.ui.classanalysis.parser.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class TypeRequirement {
    private String name;
    @JsonProperty("type_arguments")
    private List<TypeRequirement> typeArguments = new ArrayList<>();
    
    

    public TypeRequirement() {
		super();
	}

	public TypeRequirement(String name, List<TypeRequirement> typeArguments) {
		super();
		this.name = name;
		this.typeArguments = typeArguments;
	}

	public static boolean compareTypeRequirementWithType(TypeRequirement typeRequirement, Type type) {
        return typeRequirement.toString().equals(type.toString());
    }

    public static boolean compareTypeRequirementWithClassOrInterfaceType(TypeRequirement typeRequirement, ClassOrInterfaceType type) {
//        System.out.println(type.toString());
//        System.out.println(typeRequirement.toString());
        return typeRequirement.toString().equals(type.toString());
    }

    @Override
    public String toString() {
        if (typeArguments == null || typeArguments.isEmpty()) {
            return name;
        } else if (typeArguments.size() == 1) {
            return name + "<" + typeArguments.get(0).toString() + ">";
        } else {
            StringBuilder builder = new StringBuilder(name);
            builder.append("<");
            for (TypeRequirement typeRequirement : typeArguments) {
                builder.append(typeRequirement.toString());
                if ((typeArguments.indexOf(typeRequirement) + 1) != typeArguments.size()) {
                    builder.append(", ");
                }
            }
            builder.append(">");
            return builder.toString();
        }
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TypeRequirement> getTypeArguments() {
		return typeArguments;
	}

	public void setTypeArguments(List<TypeRequirement> typeArguments) {
		this.typeArguments = typeArguments;
	}
    
    
}
