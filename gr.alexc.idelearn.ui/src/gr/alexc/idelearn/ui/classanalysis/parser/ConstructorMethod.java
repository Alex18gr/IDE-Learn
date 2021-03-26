package gr.alexc.idelearn.ui.classanalysis.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.javaparser.ast.stmt.BlockStmt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ConstructorMethod {
    protected List<Modifier> modifiers;
    protected List<Parameter> parameters;
    protected BlockStmt blockStmt;
    
    

    public ConstructorMethod() {
		super();
	}



	public ConstructorMethod(List<Modifier> modifiers, List<Parameter> parameters, BlockStmt blockStmt) {
		super();
		this.modifiers = modifiers;
		this.parameters = parameters;
		this.blockStmt = blockStmt;
	}



	public List<String> getParameterTypeList() {
        List<String> paramStrings = new ArrayList<>();
        for (Parameter p : parameters) {
            paramStrings.add(p.getType().toString());
        }
        Collections.sort(paramStrings);
        return paramStrings;
    }



	public List<Modifier> getModifiers() {
		return modifiers;
	}



	public void setModifiers(List<Modifier> modifiers) {
		this.modifiers = modifiers;
	}



	public List<Parameter> getParameters() {
		return parameters;
	}



	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}



	public BlockStmt getBlockStmt() {
		return blockStmt;
	}



	public void setBlockStmt(BlockStmt blockStmt) {
		this.blockStmt = blockStmt;
	}
	
	
}
