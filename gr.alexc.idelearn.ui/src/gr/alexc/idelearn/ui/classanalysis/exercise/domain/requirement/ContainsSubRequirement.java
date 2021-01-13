package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import org.eclipse.osgi.util.NLS;

import com.fasterxml.jackson.annotation.JsonProperty;

import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;
import gr.alexc.idelearn.ui.messages.Messages;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContainsSubRequirement extends AbstractSubRequirement {

	@JsonProperty("main_class_id")
	private ClassRequirement mainClass;

	@JsonProperty("contain_class_id")
	private ClassRequirement containClass;

	@JsonProperty("relation_type")
	private RelationType relationType;

	@Override
	public String getDescription() {

		if (relationType == RelationType.ONE_TO_ONE) {
			return NLS.bind(Messages.reqContains, new Object[] { Messages.getClassOrInterfaceText(mainClass),
					mainClass.getName(), containClass.getName() });
//            return "The class \"" + mainClass.getName() + "\" must contain a reference of the class \""
//                    + containClass.getName() + "\".";
		} else if (relationType == RelationType.ONE_TO_MANY) {
			return NLS.bind(Messages.reqContainsMany, new Object[] { Messages.getClassOrInterfaceText(mainClass),
					mainClass.getName(), containClass.getName() });
//            return "The class \"" + mainClass.getName() + "\" must contain a collection of the class \""
//                    + containClass.getName() + "\".";
		}

		return "";
	}

	@Override
	public boolean checkRequirement(ClassEntity classEntity) {
		return classEntity.hasA(containClass.getName());
	}

	private enum RelationType {
		@JsonProperty("one_to_one")
		ONE_TO_ONE, @JsonProperty("one_to_many")
		ONE_TO_MANY
	}
}
