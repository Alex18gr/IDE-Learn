package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import com.fasterxml.jackson.annotation.JsonProperty;

import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;
import gr.alexc.idelearn.ui.classanalysis.parser.Method;
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
        return "Class \"" + mainClass.getName() + "\" has a method of type \"" + method.getType() + "\" and name \"" + method.getName() + "\".";
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
