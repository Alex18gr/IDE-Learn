package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import java.util.List;

import org.eclipse.osgi.util.NLS;

import com.fasterxml.jackson.annotation.JsonProperty;

import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;
import gr.alexc.idelearn.ui.classanalysis.parser.ConstructorMethod;
import gr.alexc.idelearn.ui.messages.Messages;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class ClassHasConstructorRequirement extends AbstractSubRequirement {

	@JsonProperty("main_class_id")
	private ClassRequirement mainClass;

	@JsonProperty("constructor_req")
	private ConstructorRequirement constructor;

	@Override
	public String getDescription() {
//        return "Class \"" + mainClass.getName() + "\" has a constructor with arguments " + getArgumentsString() + ".";
		if (constructor.getParameters().isEmpty()) {
			return NLS.bind(Messages.reqConstructorVoid,
					new Object[] { Messages.getClassOrInterfaceText(mainClass), mainClass.getName(), Messages.getModifiersList(constructor.getModifiers(), "M") });
		} else {
			return NLS.bind(Messages.reqConstructor,
					new Object[] { Messages.getClassOrInterfaceText(mainClass), mainClass.getName(), Messages.getModifiersList(constructor.getModifiers(), "M"),
							Messages.getMethodParametersString(constructor.getParameters()) });
		}
		
	}

	private String getArgumentsString() {
		List<ParameterRequirement> params = constructor.getParameters();
		StringBuilder builder = new StringBuilder("(");
		for (ParameterRequirement p : params) {
			builder.append(p.getType());
			if (params.indexOf(p) != params.size() - 1) {
				builder.append(", ");
			}
		}
		builder.append(")");
		return builder.toString();
	}

	@Override
	public boolean checkRequirement(ClassEntity classEntity) {
		for (ConstructorMethod cm : classEntity.getConstructors()) {
			if (constructor.checkMethod(cm)) {
				return true;
			}
		}
		return false;
	}

	public ClassRequirement getMainClass() {
		return mainClass;
	}

	public void setMainClass(ClassRequirement mainClass) {
		this.mainClass = mainClass;
	}

	public ConstructorRequirement getConstructor() {
		return constructor;
	}

	public void setConstructor(ConstructorRequirement constructor) {
		this.constructor = constructor;
	}
	
	
}
