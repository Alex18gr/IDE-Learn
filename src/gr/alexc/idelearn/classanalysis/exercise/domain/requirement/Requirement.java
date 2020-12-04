package gr.alexc.idelearn.classanalysis.exercise.domain.requirement;

import gr.alexc.idelearn.classanalysis.parser.ClassEntity;

public interface Requirement {

	String getDescription();

    boolean checkRequirement(ClassEntity classEntity);
    
}
