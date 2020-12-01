package gr.alexc.idelearn.learn;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import gr.alexc.idelearn.classanalysis.exercise.domain.Exercise;
import gr.alexc.idelearn.learn.listener.MultiChangeType;
import gr.alexc.idelearn.learn.listener.MultiExerciseChangedEvent;
import gr.alexc.idelearn.learn.listener.MultiExerciseChangedListener;
import gr.alexc.idelearn.learn.listener.SingleChangeType;
import gr.alexc.idelearn.learn.listener.SingleExerciseChangeEvent;
import gr.alexc.idelearn.learn.listener.SingleExerciseChangedListener;

public class LearnPlugin {

	private PropertyChangeSupport support;
	private List<Exercise> workspaceExercises = new ArrayList<>();

	private List<MultiExerciseChangedListener> multiExerciseEventObservers = new ArrayList<>();
	private List<SingleExerciseChangedListener> singleExerciseEventObservers = new ArrayList<>();

	private static LearnPlugin instance = null;

	public static LearnPlugin getInstance() {
		if (instance == null) {
			instance = new LearnPlugin();
		}
		return instance;
	}

	private LearnPlugin() {
		// support = new PropertyChangeSupport(this);
	}

	public void addExercise(Exercise exercise) {
		if (!checkExerciseExists(exercise.getId())) {
			this.workspaceExercises.add(exercise);
			this.notifySingleExerciseObservers(SingleChangeType.ADDED_EXERCISE, exercise);
		}
	}

	public void removeExercise(Exercise exercise) {
		if (checkExerciseExists(exercise.getId())) {
			this.workspaceExercises.remove(exercise);
			this.notifySingleExerciseObservers(SingleChangeType.REMOVED_EXERCISE, exercise);
		}
	}

	public void removeExerciseById(String exerciseId) {
		Exercise deleteExercise = getExerciseById(exerciseId);
		if (deleteExercise != null) {
			removeExercise(deleteExercise);
		}
	}

	/**
	 * return all opened workspace exercises
	 * 
	 * @return the exercises list
	 */
	public List<Exercise> getWorkspaceExercises() {
		return this.workspaceExercises;
	}

	public Exercise getExerciseById(String exerciseId) {
		for (Exercise exercise : workspaceExercises) {
			if (exercise.getId().equals(exerciseId)) {
				return exercise;
			}
		}
		return null;
	}

	public boolean checkExerciseExists(String exerciseId) {
		for (Exercise exercise : workspaceExercises) {
			if (exercise.getId().equals(exerciseId)) {
				return true;
			}
		}
		return false;
	}

	// Listeners methods

	public void addSingleExerciseChangedListener(SingleExerciseChangedListener listener) {
		this.singleExerciseEventObservers.add(listener);
	}

	public void addMultiExerciseChangedListener(MultiExerciseChangedListener listener) {
		this.multiExerciseEventObservers.add(listener);
	}

	public void removeSingleExerciseChangedListener(SingleExerciseChangedListener listener) {
		this.singleExerciseEventObservers.remove(listener);
	}

	public void removeMultiExerciseChangedListener(MultiExerciseChangedListener listener) {
		this.multiExerciseEventObservers.remove(listener);
	}

	private void notifySingleExerciseObservers(SingleChangeType type, Exercise exercise) {
		SingleExerciseChangeEvent event = new SingleExerciseChangeEvent(type, exercise);
		for (SingleExerciseChangedListener listener : singleExerciseEventObservers) {
			listener.exerciseChanged(event);
		}
	}

	private void notifyMultiExerciseObservers(MultiChangeType type, Collection<Exercise> exercises) {
		MultiExerciseChangedEvent event = new MultiExerciseChangedEvent(type, exercises);
		for (MultiExerciseChangedListener listener : multiExerciseEventObservers) {
			listener.exerciseChanged(event);
		}
	}

	// -----------------------------
}
