package gr.alexc.idelearn.ui.classanalysis.parser;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.NodeList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Type {
    private String name;
    private List<Type> typeArguments;

    public static Type getTypeFromClassOrInterfaceType(com.github.javaparser.ast.type.Type t) {
        Type type = new Type();
        if (t.isClassOrInterfaceType()) {
            type.setName(t.asClassOrInterfaceType().getNameAsString());
            if (t.asClassOrInterfaceType().getTypeArguments().isPresent()) {
                type.setTypeArguments(getTypesFromClassOrInterfaceTypeList(t.asClassOrInterfaceType().getTypeArguments().get()));
            }
        } else {
            type.setName(t.asString());
        }

        return type;
    }

    public static List<Type> getTypesFromClassOrInterfaceTypeList(NodeList<com.github.javaparser.ast.type.Type> types) {
        List<Type> typeList = new ArrayList<>();
        for (com.github.javaparser.ast.type.Type classOrInterfaceType : types) {
            typeList.add(getTypeFromClassOrInterfaceType(classOrInterfaceType.asClassOrInterfaceType()));
        }
        return typeList;
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
            for (Type typeRequirement : typeArguments) {
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
