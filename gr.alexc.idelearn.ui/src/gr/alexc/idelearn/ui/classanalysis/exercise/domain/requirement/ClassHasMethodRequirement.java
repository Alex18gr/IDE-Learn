package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import org.eclipse.osgi.util.NLS;

import com.fasterxml.jackson.annotation.JsonProperty;

import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;
import gr.alexc.idelearn.ui.classanalysis.parser.Method;
import gr.alexc.idelearn.ui.messages.Messages;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClassHasMethodRequirement extends AbstractSubRequirement {

	@JsonProperty("main_class_id")
	private ClassRequirement mainClass;

	@JsonProperty("method")
	private MethodRequirement method;

	@Override
	public String getDescription() {
//        return "Class \"" + mainClass.getName() + "\" has a method of type \"" + method.getType() + "\" and name \"" + method.getName() + "\".";
		if (method.getType().getName().equals("void")) {
			return NLS.bind(Messages.reqMethodVoid,
					new Object[] { mainClass.getName(), Messages.getModifiersList(method.getModifiers(), "F"),
							method.getName(), Messages.getMethodParametersString(method.getParameters()) });
		} else {
			return NLS.bind(Messages.reqMethod,
					new Object[] { mainClass.getName(), Messages.getModifiersList(method.getModifiers(), "F"),
							method.getName(), Messages.getMethodParametersString(method.getParameters()), method.getType() });
		}
	}

	@Override
	public boolean checkRequirement(ClassEntity classEntity) {
		for (Method m : classEntity.getMethods()) {
			if (method.checkMethod(m)) {
				return true;
			}
		}
		return false;
	}
}
