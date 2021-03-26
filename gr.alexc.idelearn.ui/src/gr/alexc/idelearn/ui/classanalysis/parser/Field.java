package gr.alexc.idelearn.ui.classanalysis.parser;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class Field {
    private String name;
    private List<Modifier> modifiers;
    private Type type;
    
    
    
	public Field() {
		super();
	}



	public Field(String name, List<Modifier> modifiers, Type type) {
		super();
		this.name = name;
		this.modifiers = modifiers;
		this.type = type;
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



	public Type getType() {
		return type;
	}



	public void setType(Type type) {
		this.type = type;
	}
    
    
}
