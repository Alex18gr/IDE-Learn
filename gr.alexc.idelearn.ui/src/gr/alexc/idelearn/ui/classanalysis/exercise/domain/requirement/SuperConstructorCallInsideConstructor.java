package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.osgi.util.NLS;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import gr.alexc.idelearn.ui.classanalysis.parser.ClassChecker;
import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;
import gr.alexc.idelearn.ui.classanalysis.parser.ConstructorMethod;
import gr.alexc.idelearn.ui.messages.Messages;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SuperConstructorCallInsideConstructor extends AbstractSubRequirement {

	@JsonProperty("main_class_id")
	private ClassRequirement mainClass;

    @JsonProperty("constructor_method")
	private ConstructorRequirement constructor;

	@JsonProperty("call_constructor")
	private ConstructorRequirement callConstructor;

	private String getArgumentsString(ConstructorRequirement constructor) {
		List<ParameterRequirement> params = constructor.getParameters();
		StringBuilder builder = new StringBuilder("(");
		for (ParameterRequirement p : params) {
			builder.append(p.getType());
			if (params.indexOf(p) != params.size() - 1) {
				builder.append(", ");
			}
		}
		builder.append(")");
		return builder.toString();
	}

	@Override
	public String getDescription() {
//		return "Constructor \"" + getArgumentsString(constructor) + "\" must have a call to super constructor \""
//				+ getArgumentsString(callConstructor) + "\"";
		return NLS.bind(Messages.reqConstructorCallConstructor,
				new Object[] { mainClass.getName(), Messages.getMethodParametersString(constructor.getParameters()),
						Messages.getMethodParametersString(callConstructor.getParameters()) });
	}

	@Override
	public boolean checkRequirement(ClassEntity classEntity) {

		for (ConstructorMethod m : classEntity.getConstructors()) {
			if (constructor.checkMethod(m)) {
				ExplicitConstructorCallVisitor visitor = new ExplicitConstructorCallVisitor();
				m.getBlockStmt().accept(visitor, null);
				List<ConstructorMethod> methodsFound = visitor.getConstructorCallsFound();
				for (ConstructorMethod method : methodsFound) {
					if (callConstructor.checkMethod(method)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	class ExplicitConstructorCallVisitor extends VoidVisitorAdapter<Void> {
		private final List<ConstructorMethod> constructorCallsFound;

		public ExplicitConstructorCallVisitor() {
			super();
			constructorCallsFound = new ArrayList<>();
		}

		public List<ConstructorMethod> getConstructorCallsFound() {
			return constructorCallsFound;
		}

		@Override
		public void visit(ExplicitConstructorInvocationStmt n, Void arg) {

			if (!n.isThis()) {
				Optional<ConstructorDeclaration> constructorDeclarationOptional = n.resolve().toAst();
				if (constructorDeclarationOptional.isPresent()) {
					ConstructorMethod constructorMethod = ClassChecker
							.getConstructorMethodFromConstructorMethodDeclaration(constructorDeclarationOptional.get());
					constructorCallsFound.add(constructorMethod);
				}
			}

			super.visit(n, arg);
		}
	}
}
