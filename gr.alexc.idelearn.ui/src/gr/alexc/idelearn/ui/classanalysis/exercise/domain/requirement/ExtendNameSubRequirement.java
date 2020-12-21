package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import com.fasterxml.jackson.annotation.JsonProperty;

import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExtendNameSubRequirement extends AbstractSubRequirement {

    @JsonProperty("main_class_id")
    private ClassRequirement mainClass;

    @JsonProperty("extend_type_name")
    private String extendTypeName;

    @Override
    public String getDescription() {
        return "The class \"" + mainClass.getName() + "\" must extend class \"" + extendTypeName + "\".";
    }

    @Override
    public boolean checkRequirement(ClassEntity classEntity) {
        return classEntity.getExtendClassesName().contains(extendTypeName);
    }
}
