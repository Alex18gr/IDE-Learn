package gr.alexc.idelearn.ui.learn.listener;

import java.util.Collection;

import gr.alexc.idelearn.ui.classanalysis.exercise.domain.Exercise;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MultiExerciseChangedEvent {
	private MultiChangeType changeType;
	private Collection<Exercise> exercise;
}