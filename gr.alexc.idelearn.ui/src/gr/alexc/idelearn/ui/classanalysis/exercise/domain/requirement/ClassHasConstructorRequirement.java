package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;
import gr.alexc.idelearn.ui.classanalysis.parser.ConstructorMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClassHasConstructorRequirement extends AbstractSubRequirement {

    @JsonProperty("main_class_id")
    private ClassRequirement mainClass;

    @JsonProperty("constructor")
    private ConstructorRequirement constructor;

    @Override
    public String getDescription() {
        return "Class \"" + mainClass.getName() + "\" has a constructor with arguments " + getArgumentsString() + ".";
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
}
