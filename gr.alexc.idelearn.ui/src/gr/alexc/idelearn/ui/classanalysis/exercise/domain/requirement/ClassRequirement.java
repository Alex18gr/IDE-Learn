package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import java.util.List;

import org.eclipse.osgi.util.NLS;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;
import gr.alexc.idelearn.ui.messages.Messages;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(scope=ClassRequirement.class, generator= ObjectIdGenerators.PropertyGenerator.class, property="class_id")
public class ClassRequirement extends AbstractRequirement {

    @JsonProperty("class_id")
    private Integer classId;

    @JsonProperty("name")
    private String name;
    
    @JsonProperty("is_abstract")
    private Boolean isAbstract = false;
    
    @JsonProperty("is_interface")
    private Boolean isInterface = false;

    @JsonProperty("related_requirements")
    private List<AbstractSubRequirement> relatedRequirements;
    
    public String getClassRequirementDescription() {
        StringBuilder builder = new StringBuilder();
        if (isAbstract) {
            builder.append("Must exist an abstract class with name \"");
        } else {
            builder.append("Must exist a class with the name \"");
        }
        builder.append(name).append("\".");
        return builder.toString();
    }


    @Override
    public String getDescription() {
    	if (isInterface) {
    		return NLS.bind(Messages.reqInterface, name);
    	} else if (isAbstract) {
            return NLS.bind(Messages.reqClassAbstract, name);
        } else {
        	return NLS.bind(Messages.reqClass, name);
        }
    }

    @Override
    public boolean checkRequirement(ClassEntity classEntity) {
    	if (isInterface) {
            if (!classEntity.getIsInterface()) {
                return false;
            }
        } else if (isAbstract) {
            if (!classEntity.getIsAbstract()) {
                return false;
            }
        }
        return classEntity.getClassName().equals(name);
    }

	@Override
	public Integer getTotalSubRequirements() {
		return relatedRequirements.size();
	}

	@Override
	public List<AbstractSubRequirement> getSubRequirements() {
		return relatedRequirements;
	}
}
