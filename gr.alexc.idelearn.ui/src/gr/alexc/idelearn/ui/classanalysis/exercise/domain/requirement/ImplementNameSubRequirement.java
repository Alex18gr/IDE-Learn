package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import org.eclipse.osgi.util.NLS;

import com.fasterxml.jackson.annotation.JsonProperty;

import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;
import gr.alexc.idelearn.ui.messages.Messages;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ImplementNameSubRequirement extends AbstractSubRequirement {

    @JsonProperty("main_class_id")
    private ClassRequirement mainClass;

    @JsonProperty("implement_type_name")
    private String implementTypeName;

    @Override
    public String getDescription() {
//        return "The class \"" + mainClass.getName() + "\" must implement interface \"" + implementTypeName + "\".";
    	return NLS.bind(Messages.reqImplementName, new Object[] { Messages.getClassOrInterfaceText(mainClass), mainClass.getName(), implementTypeName });
    }

    @Override
    public boolean checkRequirement(ClassEntity classEntity) {
        return classEntity.getImplementClassesName().contains(implementTypeName);
    }

	public ClassRequirement getMainClass() {
		return mainClass;
	}

	public void setMainClass(ClassRequirement mainClass) {
		this.mainClass = mainClass;
	}

	public String getImplementTypeName() {
		return implementTypeName;
	}

	public void setImplementTypeName(String implementTypeName) {
		this.implementTypeName = implementTypeName;
	}
    
    
}
