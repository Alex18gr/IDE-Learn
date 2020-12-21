package gr.alexc.idelearn.ui.classanalysis.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.javaparser.ast.stmt.BlockStmt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConstructorMethod {
    protected List<Modifier> modifiers;
    protected List<Parameter> parameters;
    protected BlockStmt blockStmt;

    public List<String> getParameterTypeList() {
        List<String> paramStrings = new ArrayList<>();
        for (Parameter p : parameters) {
            paramStrings.add(p.getType().toString());
        }
        Collections.sort(paramStrings);
        return paramStrings;
    }
}
