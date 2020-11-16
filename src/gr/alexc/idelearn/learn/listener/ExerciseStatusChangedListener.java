package gr.alexc.idelearn.learn.listener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class ExerciseStatusChangedListener implements IExerciseStatusChangedListener, PropertyChangeListener {
	
	public void propertyChange(PropertyChangeEvent evt) {
      this.exerciseStatusChanged(null);
    }
	
	public abstract void exerciseStatusChanged(IExerciseStatusChangeEvent status);

}
