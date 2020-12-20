package gr.alexc.idelearn.ui.classanalysis.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class ClassRelation {

    private ClassEntity classEntity;
    private ClassEntity relatedClass;
    private RelationType relationType;

    public enum RelationType {
        ONE_TO_ONE,
        ONE_TO_MANY,
        MANY_TO_ONE,
        MANY_TO_MANY
    }
}
