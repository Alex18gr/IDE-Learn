package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;

public interface Requirement {

	String getDescription();

    boolean checkRequirement(ClassEntity classEntity);
    
}
