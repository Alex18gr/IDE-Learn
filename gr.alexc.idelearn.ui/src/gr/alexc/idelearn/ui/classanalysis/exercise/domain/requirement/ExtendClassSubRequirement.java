package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import org.eclipse.osgi.util.NLS;

import com.fasterxml.jackson.annotation.JsonProperty;

import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;
import gr.alexc.idelearn.ui.messages.Messages;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ExtendClassSubRequirement extends AbstractSubRequirement {

	@JsonProperty("main_class_id")
	private ClassRequirement mainClass;

	@JsonProperty("extend_class_id")
	private ClassRequirement extendClass;

	public String getDescription() {
		return NLS.bind(Messages.reqExtend, new Object[] { Messages.getClassOrInterfaceText(mainClass), mainClass.getName(), extendClass.getName() });
//        return "The class \"" + mainClass.getName() + "\" must extend class \"" + extendClass.getName() + "\".";
	}

	@Override
	public boolean checkRequirement(ClassEntity classEntity) {
		return classEntity.extendsA(extendClass.getName());
	}

	public ClassRequirement getMainClass() {
		return mainClass;
	}

	public void setMainClass(ClassRequirement mainClass) {
		this.mainClass = mainClass;
	}

	public ClassRequirement getExtendClass() {
		return extendClass;
	}

	public void setExtendClass(ClassRequirement extendClass) {
		this.extendClass = extendClass;
	}
	
	

}
