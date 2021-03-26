package gr.alexc.idelearn.ui.learn.listener;

import java.util.Collection;

import gr.alexc.idelearn.ui.classanalysis.exercise.domain.Exercise;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MultiExerciseChangedEvent {
	private MultiChangeType changeType;
	private Collection<Exercise> exercise;
	
	public MultiExerciseChangedEvent(MultiChangeType changeType, Collection<Exercise> exercise) {
		super();
		this.changeType = changeType;
		this.exercise = exercise;
	}
	
	public MultiExerciseChangedEvent() {
		super();
	}

	public MultiChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(MultiChangeType changeType) {
		this.changeType = changeType;
	}

	public Collection<Exercise> getExercise() {
		return exercise;
	}

	public void setExercise(Collection<Exercise> exercise) {
		this.exercise = exercise;
	}
	
	
	
	
}
