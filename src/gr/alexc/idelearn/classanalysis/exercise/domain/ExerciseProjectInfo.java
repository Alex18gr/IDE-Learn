package gr.alexc.idelearn.classanalysis.exercise.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import gr.alexc.idelearn.classanalysis.exercise.domain.requirement.AbstractRequirement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExerciseProjectInfo {
	
	private String title;
	
	@JsonProperty("starting_project")
	private Boolean statingProjectExists;

}
