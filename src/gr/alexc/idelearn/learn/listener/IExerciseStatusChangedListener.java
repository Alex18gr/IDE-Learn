package gr.alexc.idelearn.learn.listener;

import gr.alexc.idelearn.learn.ExerciseStatus;

public interface IExerciseStatusChangedListener {
	
	void exerciseStatusChanged(IExerciseStatusChangeEvent status);
	
}
