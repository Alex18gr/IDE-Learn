package gr.alexc.idelearn.learn.listener;

import gr.alexc.idelearn.learn.ExerciseStatus;

public interface IExerciseStatusChangeEvent {
	
	int getType();
	
	ExerciseStatus getExerciseStatus();
	
}
