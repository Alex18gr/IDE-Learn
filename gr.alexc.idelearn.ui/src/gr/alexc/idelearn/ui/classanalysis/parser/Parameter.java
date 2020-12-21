package gr.alexc.idelearn.ui.classanalysis.parser;

import com.github.javaparser.ast.NodeList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Parameter {
    private String name;
    private Type type;

    public static List<Parameter> getParametersFromParameters(NodeList<com.github.javaparser.ast.body.Parameter> parameters) {
        List<Parameter> parameterList = new ArrayList<>();
        for (com.github.javaparser.ast.body.Parameter parameter : parameters) {
            Parameter p = new Parameter();
            p.setName(parameter.getNameAsString());
            p.setType(Type.getTypeFromClassOrInterfaceType(parameter.getType()));
            parameterList.add(p);
        }
        return parameterList;
    }
}
