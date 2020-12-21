package gr.alexc.idelearn.ui.classanalysis.parser;

import com.github.javaparser.ast.stmt.BlockStmt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Method {

    protected String name;
    protected List<Modifier> modifiers;
    protected List<Parameter> parameters;
    protected Type type;
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
