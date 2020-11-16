package gr.alexc.idelearn.learn;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class LearnPlugin {
	
	private PropertyChangeSupport support;
	
	public LearnPlugin() {
		support = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }
 
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

}
