package gr.alexc.idelearn.learn;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import gr.alexc.idelearn.classanalysis.exercise.domain.Exercise;

public class LearnPlugin {
	
	private PropertyChangeSupport support;
	private List<Exercise> workspaceExercises = new ArrayList<>();
	
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
		}
	}
	
	public boolean checkExerciseExists(String exerciseId) {
		for (Exercise exercise : workspaceExercises) {
			if (exercise.getId().equals(exerciseId)) {
				return true;
			}
		}
		return false;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }
 
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

}
