package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import gr.alexc.idelearn.ui.classanalysis.parser.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TypeRequirement {
    private String name;
    @JsonProperty("type_arguments")
    private List<TypeRequirement> typeArguments;

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
                if (typeArguments.indexOf(typeRequirement) != typeArguments.size()) {
                    builder.append(", ");
                }
            }
            builder.append(">");
            return builder.toString();
        }
    }
}
