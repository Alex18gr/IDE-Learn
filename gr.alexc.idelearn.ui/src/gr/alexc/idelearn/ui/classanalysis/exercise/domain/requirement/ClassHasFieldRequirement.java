package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import com.fasterxml.jackson.annotation.JsonProperty;

import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;
import gr.alexc.idelearn.ui.classanalysis.parser.Field;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClassHasFieldRequirement extends AbstractSubRequirement {

    @JsonProperty("main_class_id")
    private ClassRequirement mainClass;

    private FieldRequirement field;

    @Override
    public String getDescription() {
        return "Class \"" + mainClass.getName() + "\" has a field of type \"" + field.getType() + "\" and name \"" + field.getName() + "\".";
    }

    @Override
    public boolean checkRequirement(ClassEntity classEntity) {
        for (Field f : classEntity.getFields()) {
            if (field.checkField(f)) {
                return true;
            }
        }
        return false;
//        ClassOrInterfaceDeclaration classDeclaration = classEntity.getClassDeclaration();
//        List<FieldDeclaration> fieldDeclarationList = classDeclaration.getFields();
//        for (Field field : classEntity.getFields()) {
//            if (field.getName().equals(this.field.getName())) {
//
//            }
//        }
//        return false;
    }

}
