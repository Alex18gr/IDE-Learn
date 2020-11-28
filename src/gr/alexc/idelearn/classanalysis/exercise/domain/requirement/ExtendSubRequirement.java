package gr.alexc.idelearn.classanalysis.exercise.domain.requirement;

import com.fasterxml.jackson.annotation.JsonProperty;

import gr.alexc.idelearn.classanalysis.parser.ClassEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExtendSubRequirement extends AbstractSubRequirement {

    @JsonProperty("main_class_id")
    private ClassRequirement mainClass;

    @JsonProperty("extend_class_id")
    private ClassRequirement extendClass;

    public String getDescription() {
        return "The class \"" + mainClass.getName() + "\" must extend class \"" + extendClass.getName() + "\".";
    }

    @Override
    public boolean checkRequirement(ClassEntity classEntity) {
        return classEntity.extendsA(extendClass.getName());
    }


}
