package gr.alexc.idelearn.ui.classanalysis.parser;

import com.github.javaparser.ast.stmt.BlockStmt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Method {

    protected String name;
    protected List<Modifier> modifiers;
    protected List<Parameter> parameters;
    protected Type type;
    protected BlockStmt blockStmt;
    
    

    public Method() {
		super();
	}



	public Method(String name, List<Modifier> modifiers, List<Parameter> parameters, Type type, BlockStmt blockStmt) {
		super();
		this.name = name;
		this.modifiers = modifiers;
		this.parameters = parameters;
		this.type = type;
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



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
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



	public Type getType() {
		return type;
	}



	public void setType(Type type) {
		this.type = type;
	}



	public BlockStmt getBlockStmt() {
		return blockStmt;
	}



	public void setBlockStmt(BlockStmt blockStmt) {
		this.blockStmt = blockStmt;
	}
	
	


}
