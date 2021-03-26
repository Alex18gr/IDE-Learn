package gr.alexc.idelearn.ui.classanalysis.exercise.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement.AbstractRequirement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ExerciseProjectInfo {
	
	private String title;
	
	@JsonProperty("starting_project")
	private Boolean statingProjectExists;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getStatingProjectExists() {
		return statingProjectExists;
	}

	public void setStatingProjectExists(Boolean statingProjectExists) {
		this.statingProjectExists = statingProjectExists;
	}
	
	

}
