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
public class ExtendNameSubRequirement extends AbstractSubRequirement {

	@JsonProperty("main_class_id")
	private ClassRequirement mainClass;

	@JsonProperty("extend_type_name")
	private String extendTypeName;

	@Override
	public String getDescription() {
		return NLS.bind(Messages.reqExtend, new Object[] { mainClass.getName(), extendTypeName });
	}

	@Override
	public boolean checkRequirement(ClassEntity classEntity) {
		return classEntity.getExtendClassesName().contains(extendTypeName);
	}
}
