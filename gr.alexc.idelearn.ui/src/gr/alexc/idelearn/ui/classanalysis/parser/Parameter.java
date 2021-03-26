package gr.alexc.idelearn.ui.classanalysis.parser;

import com.github.javaparser.ast.NodeList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Parameter {
    private String name;
    private Type type;
    
    

    public Parameter() {
		super();
	}



	public Parameter(String name, Type type) {
		super();
		this.name = name;
		this.type = type;
	}



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



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public Type getType() {
		return type;
	}



	public void setType(Type type) {
		this.type = type;
	}
	
	
	
}
