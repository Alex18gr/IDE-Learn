package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import com.fasterxml.jackson.annotation.JsonProperty;

import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class IsASubRequirement extends AbstractSubRequirement {

    @JsonProperty("main_class_id")
    private ClassRequirement mainClass;

    @JsonProperty("is_a_class_id")
    private ClassRequirement isAClass;

    @Override
    public String getDescription() {
        return "The class \"" + mainClass.getName() + "\" must be a/an \"" + isAClass.getName() + "\".";
    }

    @Override
    public boolean checkRequirement(ClassEntity classEntity) {
        return classEntity.isA(isAClass.getName());
    }

	public ClassRequirement getMainClass() {
		return mainClass;
	}

	public void setMainClass(ClassRequirement mainClass) {
		this.mainClass = mainClass;
	}

	public ClassRequirement getIsAClass() {
		return isAClass;
	}

	public void setIsAClass(ClassRequirement isAClass) {
		this.isAClass = isAClass;
	}
    
    
}
