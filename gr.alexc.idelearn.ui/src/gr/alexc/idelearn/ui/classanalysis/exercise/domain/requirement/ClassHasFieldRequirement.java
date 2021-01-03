package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import java.util.Collections;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;

import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;
import gr.alexc.idelearn.ui.classanalysis.parser.Field;
import gr.alexc.idelearn.ui.classanalysis.parser.Method;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClassHasFieldRequirement extends AbstractSubRequirement {

	@JsonProperty("main_class_id")
	private ClassRequirement mainClass;

	private FieldRequirement field;

	@JsonProperty("include_setter")
	private Boolean includeSetter = false;

	@JsonProperty("include_getter")
	private Boolean includeGetter = false;

	@Override
	public String getDescription() {
		StringBuilder builder = new StringBuilder();
		builder.append("Class \"").append(mainClass.getName()).append("\" has a field of type \"")
				.append(field.getType()).append("\" and name \"").append(field.getName()).append("\"");
		if (includeSetter || includeGetter) {
			builder.append(" included");
			if (includeSetter && includeGetter) {
				builder.append(" setter and getter methods.");
			} else if (includeSetter) {
				builder.append(" setter method.");
			} else {
				builder.append(" getter method.");
			}
		}
		return builder.toString();
	}

	@Override
	public boolean checkRequirement(ClassEntity classEntity) {

		// check if the field exists
		boolean found = false;
		for (Field f : classEntity.getFields()) {

			if (field.checkField(f)) {
				found = true;
			}
		}
		if (!found)
			return false;

		// check the getter
		if (includeGetter != null && includeGetter) {
			MethodRequirement getterMethodRequirement = getGetterRequirementMethod();
			found = false;
			for (Method method : classEntity.getMethods()) {
				if (getterMethodRequirement.checkMethod(method)) {
					found = true;
				}
			}
			if (!found)
				return false;
		}

		// check the setter
		if (includeSetter != null && includeSetter) {
			MethodRequirement setterRequirementMethod = getSetterRequirementMethod();
			found = false;
			for (Method method : classEntity.getMethods()) {
				if (setterRequirementMethod.checkMethod(method)) {
					found = true;
				}
			}
			if (!found)
				return false;
		}

		// when all the requirements are satisfied return true
		return true;
	}

	private MethodRequirement getGetterRequirementMethod() {
		MethodRequirement methodRequirement = new MethodRequirement();

		// set the method name
		methodRequirement.setName(getFieldMethodName("get", field.getName()));

		// set the modifiers
		methodRequirement.setModifiers(Collections.singletonList("PUBLIC"));

		// set the return type
		methodRequirement.setType(field.getType());

		methodRequirement.setParameters(Collections.emptyList());

		return methodRequirement;
	}

	private MethodRequirement getSetterRequirementMethod() {
		MethodRequirement methodRequirement = new MethodRequirement();

		// set the method name
		methodRequirement.setName(getFieldMethodName("set", field.getName()));

		// set the modifiers
		methodRequirement.setModifiers(Collections.singletonList("PUBLIC"));

		// set the return type
		TypeRequirement typeRequirement = new TypeRequirement();
		typeRequirement.setName("void");
		methodRequirement.setType(typeRequirement);

		// set the method parameters
		ParameterRequirement parameterRequirement = new ParameterRequirement();
		parameterRequirement.setName("obj");
		parameterRequirement.setType(field.getType());
		methodRequirement.setParameters(Collections.singletonList(parameterRequirement));

		return methodRequirement;
	}

	private String getFieldMethodName(String methodPrefix, String fieldName) {
		if (fieldName.length() == 1) {
			return methodPrefix + fieldName.toUpperCase(Locale.ROOT);
		} else {
			if (fieldName.equals(fieldName.toUpperCase(Locale.ROOT))) {
				return methodPrefix + fieldName;
			} else {
				return methodPrefix + fieldName.substring(0, 1).toUpperCase(Locale.ROOT) + fieldName.substring(1);
			}
		}
	}

}
