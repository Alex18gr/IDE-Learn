package gr.alexc.idelearn.ui.classanalysis.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ClassRelation {

    private ClassEntity classEntity;
    private ClassEntity relatedClass;
    private RelationType relationType;
    
    

    public ClassRelation() {
		super();
	}



	public ClassRelation(ClassEntity classEntity, ClassEntity relatedClass, RelationType relationType) {
		super();
		this.classEntity = classEntity;
		this.relatedClass = relatedClass;
		this.relationType = relationType;
	}



	public enum RelationType {
        ONE_TO_ONE,
        ONE_TO_MANY,
        MANY_TO_ONE,
        MANY_TO_MANY
    }



	public ClassEntity getClassEntity() {
		return classEntity;
	}



	public void setClassEntity(ClassEntity classEntity) {
		this.classEntity = classEntity;
	}



	public ClassEntity getRelatedClass() {
		return relatedClass;
	}



	public void setRelatedClass(ClassEntity relatedClass) {
		this.relatedClass = relatedClass;
	}



	public RelationType getRelationType() {
		return relationType;
	}



	public void setRelationType(RelationType relationType) {
		this.relationType = relationType;
	}
	
	
	
}
