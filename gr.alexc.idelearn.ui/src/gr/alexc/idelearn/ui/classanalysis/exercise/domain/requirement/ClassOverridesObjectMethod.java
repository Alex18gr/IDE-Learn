package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import java.util.Collections;

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
public class ClassOverridesObjectMethod extends AbstractSubRequirement {

	@JsonProperty("main_class_id")
	private ClassRequirement mainClass;

	@JsonProperty("object_method")
	private ObjectMethod objectMethod;

	@Override
	public String getDescription() {
//		return "Class \"" + mainClass.getName() + "\" overrides the Object method " + objectMethod.name() + ".";
		return NLS.bind(Messages.reqOverrideObjectMethod, new Object[] {Messages.getClassOrInterfaceText(mainClass), mainClass.getName(), getMethodName() });
	}
	
	private String getMethodName() {
		switch (objectMethod) {
		case CLONE:
			return "clone";
		case EQUALS:
			return "equals";
		case HASH_CODE:
			return "hashCode";
		case TO_STRING:
			return "toString";
		case COMPARABLE_COMPARE_TO:
			return "compareTo";
		default:
			return "";
		}
	}

	@Override
	public boolean checkRequirement(ClassEntity classEntity) {
		MethodRequirement methodRequirement = null;
		switch (objectMethod) {
		case CLONE:
			methodRequirement = getCloneMethodRequirement();
			break;
		case EQUALS:
			methodRequirement = getEqualsMethodRequirement();
			break;
		case HASH_CODE:
			methodRequirement = getHashCodeMethodRequirement();
			break;
		case TO_STRING:
			methodRequirement = getToStringMethodRequirement();
			break;
		case COMPARABLE_COMPARE_TO:
			methodRequirement = getComparableCompareToMethodRequirement();
			break;
		default:
			return true;
		}

		// check the method
		ClassHasMethodRequirement requirement = new ClassHasMethodRequirement();
		requirement.setMethod(methodRequirement);
		requirement.setMainClass(mainClass);
		return requirement.checkRequirement(classEntity);
	}

	private MethodRequirement getCloneMethodRequirement() {
		MethodRequirement methodRequirement = new MethodRequirement();

		// set the method name
		methodRequirement.setName("clone");

		// set the modifiers
		methodRequirement.setModifiers(Collections.singletonList("PROTECTED"));

		// set the type
		TypeRequirement typeRequirement = new TypeRequirement();
		typeRequirement.setName("Object");
		methodRequirement.setType(typeRequirement);

		return methodRequirement;
	}

	private MethodRequirement getEqualsMethodRequirement() {
		MethodRequirement methodRequirement = new MethodRequirement();

		// set the method name
		methodRequirement.setName("equals");

		// set the modifiers
		methodRequirement.setModifiers(Collections.singletonList("PUBLIC"));

		// set the return type
		TypeRequirement typeRequirement = new TypeRequirement();
		typeRequirement.setName("boolean");
		methodRequirement.setType(typeRequirement);

		// set the method parameters
		ParameterRequirement parameterRequirement = new ParameterRequirement();
		parameterRequirement.setName("obj");
		TypeRequirement parameterType1 = new TypeRequirement();
		parameterType1.setName("Object");
		parameterRequirement.setType(parameterType1);
		methodRequirement.setParameters(Collections.singletonList(parameterRequirement));

		return methodRequirement;
	}

	private MethodRequirement getHashCodeMethodRequirement() {
		MethodRequirement methodRequirement = new MethodRequirement();

		// set the method name
		methodRequirement.setName("hashCode");

		// set the modifiers
		methodRequirement.setModifiers(Collections.singletonList("PUBLIC"));

		// set the type
		TypeRequirement typeRequirement = new TypeRequirement();
		typeRequirement.setName("int");
		methodRequirement.setType(typeRequirement);

		return methodRequirement;
	}

	private MethodRequirement getToStringMethodRequirement() {
		MethodRequirement methodRequirement = new MethodRequirement();

		// set the method name
		methodRequirement.setName("toString");

		// set the modifiers
		methodRequirement.setModifiers(Collections.singletonList("PUBLIC"));

		// set the type
		TypeRequirement typeRequirement = new TypeRequirement();
		typeRequirement.setName("String");
		methodRequirement.setType(typeRequirement);

		return methodRequirement;
	}

	private MethodRequirement getComparableCompareToMethodRequirement() {
		MethodRequirement methodRequirement = new MethodRequirement();

		// set the method name
		methodRequirement.setName("compareTo");

		// set the modifiers
		methodRequirement.setModifiers(Collections.singletonList("PUBLIC"));

		// set the type
		TypeRequirement typeRequirement = new TypeRequirement();
		typeRequirement.setName("int");
		methodRequirement.setType(typeRequirement);

		// set the method parameters
		ParameterRequirement parameterRequirement = new ParameterRequirement();
		parameterRequirement.setName("obj");
		TypeRequirement parameterType1 = new TypeRequirement();
		parameterType1.setName("Object");
		parameterRequirement.setType(parameterType1);
		methodRequirement.setParameters(Collections.singletonList(parameterRequirement));

		return methodRequirement;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public enum ObjectMethod {
		CLONE, EQUALS, HASH_CODE, TO_STRING, COMPARABLE_COMPARE_TO
	}
}
