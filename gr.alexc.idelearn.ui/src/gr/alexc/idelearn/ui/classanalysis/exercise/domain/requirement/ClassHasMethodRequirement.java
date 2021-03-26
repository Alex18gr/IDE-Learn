package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import org.eclipse.osgi.util.NLS;

import com.fasterxml.jackson.annotation.JsonProperty;

import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;
import gr.alexc.idelearn.ui.classanalysis.parser.Method;
import gr.alexc.idelearn.ui.messages.Messages;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ClassHasMethodRequirement extends AbstractSubRequirement {

	@JsonProperty("main_class_id")
	private ClassRequirement mainClass;

	@JsonProperty("method")
	private MethodRequirement method;

	@JsonProperty("overriding_super_class_method")
	private Boolean overridingSuperClassMethod = false;

	@JsonProperty("overriding_super_class_method_name")
	private String overridingSuperClassMethodName = "";

	@Override
	public String getDescription() {
//        return "Class \"" + mainClass.getName() + "\" has a method of type \"" + method.getType() + "\" and name \"" + method.getName() + "\".";

		if (overridingSuperClassMethod) {
			return NLS.bind(Messages.reqMethodOverride, new Object[] { Messages.getClassOrInterfaceText(mainClass),
					mainClass.getName(), Messages.getMethodSignature(method), overridingSuperClassMethodName });
		} else {
			if (method.getType().getName().equals("void")) {
				return NLS.bind(Messages.reqMethodVoid,
						new Object[] { Messages.getClassOrInterfaceText(mainClass), mainClass.getName(),
								Messages.getModifiersList(method.getModifiers(), "F"), method.getName(),
								Messages.getMethodParametersString(method.getParameters()) });
			} else {
				return NLS.bind(Messages.reqMethod,
						new Object[] { Messages.getClassOrInterfaceText(mainClass), mainClass.getName(),
								Messages.getModifiersList(method.getModifiers(), "F"), method.getName(),
								Messages.getMethodParametersString(method.getParameters()), method.getType() });
			}
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

	public ClassRequirement getMainClass() {
		return mainClass;
	}

	public void setMainClass(ClassRequirement mainClass) {
		this.mainClass = mainClass;
	}

	public MethodRequirement getMethod() {
		return method;
	}

	public void setMethod(MethodRequirement method) {
		this.method = method;
	}

	public Boolean getOverridingSuperClassMethod() {
		return overridingSuperClassMethod;
	}

	public void setOverridingSuperClassMethod(Boolean overridingSuperClassMethod) {
		this.overridingSuperClassMethod = overridingSuperClassMethod;
	}

	public String getOverridingSuperClassMethodName() {
		return overridingSuperClassMethodName;
	}

	public void setOverridingSuperClassMethodName(String overridingSuperClassMethodName) {
		this.overridingSuperClassMethodName = overridingSuperClassMethodName;
	}
	
	
}
