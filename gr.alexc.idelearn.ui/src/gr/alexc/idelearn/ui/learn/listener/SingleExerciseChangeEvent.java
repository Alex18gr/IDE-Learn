package gr.alexc.idelearn.ui.learn.listener;

import gr.alexc.idelearn.ui.classanalysis.exercise.domain.Exercise;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SingleExerciseChangeEvent {
	private SingleChangeType changeType;
	private Exercise exercise;
	
	public SingleExerciseChangeEvent(SingleChangeType changeType, Exercise exercise) {
		super();
		this.changeType = changeType;
		this.exercise = exercise;
	}

	public SingleExerciseChangeEvent() {
		super();
	}

	public SingleChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(SingleChangeType changeType) {
		this.changeType = changeType;
	}

	public Exercise getExercise() {
		return exercise;
	}

	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}
	
	
	
	
}
