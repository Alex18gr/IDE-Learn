package gr.alexc.idelearn.ui.messages;

import java.util.List;

import org.eclipse.osgi.util.NLS;

import gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement.ClassRequirement;
import gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement.MethodRequirement;
import gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement.ParameterRequirement;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "gr.alexc.idelearn.ui.messages.messages";

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
	
	public static String getMethodSignature(MethodRequirement methodRequirement) {
		StringBuilder builder = new StringBuilder();
		builder.append(methodRequirement.getType()).append(" ");
		builder.append(methodRequirement.getName());
		builder.append(getMethodParametersString(methodRequirement.getParameters()));
		return builder.toString();
	}
	
	public static String getModifiersList(List<String> modifiers, String gender) {
		StringBuilder builder = new StringBuilder();
		for(String m : modifiers) {
			
			if (m.equals("PRIVATE")) {
				if (gender.equals("M")) {
					builder.append(reqPrivate);
				} else {
					builder.append(reqPrivateF);
				}
			}
			if (m.equals("PUBLIC")) {
				if (gender.equals("M")) {
					builder.append(reqPublic);
				} else {
					builder.append(reqPublicF);
				}
			}
			if (m.equals("PROTECTED")) {
				if (gender.equals("M")) {
					builder.append(reqProtected);
				} else {
					builder.append(reqProtectedF);
				}
			}
			if (m.equals("STATIC")) {
				if (gender.equals("M")) {
					builder.append(reqStatic);
				} else {
					builder.append(reqStaticF);
				}
			}
			if (m.equals("ABSTRACT")) {
				if (gender.equals("M")) {
					builder.append(reqAbstract);
				} else {
					builder.append(reqAbstractF);
				}
			}
			
			if((modifiers.indexOf(m) + 1) != modifiers.size()) {
				builder.append(", ");
			}
			
		}
		return builder.toString();
	}
	
	public static String getMethodParametersString(List<ParameterRequirement> requirements) {
		StringBuilder builder = new StringBuilder("(");
		for(ParameterRequirement req : requirements) {
			builder.append(req.getType());
			if((requirements.indexOf(req) + 1) != requirements.size()) {
				builder.append(", ");
			}
		}
		builder.append(")");
		return builder.toString();
	}
	
	public static String getClassOrInterfaceText(ClassRequirement classRequirement) {
		if (classRequirement.getIsInterface()) {
			return reqInterfaceText;
		} else {
			return reqClassText;
		}
	}

	public static String reqClass;
	
	public static String reqInterface;

	public static String reqClassAbstract;

	public static String reqExtend;

	public static String reqExtendName;

	public static String reqImplementName;

	public static String reqContains;
	
	public static String reqContainsMany;

	public static String reqContainsField;

	public static String reqContainsFieldGetter;

	public static String reqContainsFieldSetter;

	public static String reqContainsFieldGetterSetter;

	public static String reqPrivate;

	public static String reqPublic;

	public static String reqProtected;

	public static String reqStatic;

	public static String reqAbstract;

	public static String reqPrivateF;

	public static String reqPublicF;

	public static String reqProtectedF;

	public static String reqStaticF;

	public static String reqAbstractF;
	
	public static String reqClassText;
	
	public static String reqClassesText;
	
	public static String reqSuperClassesText;
	
	public static String reqInterfaceText;

	public static String reqMethod;

	public static String reqMethodVoid;

	public static String reqConstructor;
	
	public static String reqConstructorVoid;
	
	public static String reqMethodOverride;

	public static String reqOverrideObjectMethod;

	public static String reqMethodCallMethod;

	public static String reqConstructorCallMethod;

	public static String reqConstructorCallConstructor;

}
