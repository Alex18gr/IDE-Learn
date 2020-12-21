package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import com.fasterxml.jackson.annotation.JsonProperty;

import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImplementNameSubRequirement extends AbstractSubRequirement {

    @JsonProperty("main_class_id")
    private ClassRequirement mainClass;

    @JsonProperty("implement_type_name")
    private String implementTypeName;

    @Override
    public String getDescription() {
        return "The class \"" + mainClass.getName() + "\" must implement interface \"" + implementTypeName + "\".";
    }

    @Override
    public boolean checkRequirement(ClassEntity classEntity) {
        return classEntity.getImplementClassesName().contains(implementTypeName);
    }
}
