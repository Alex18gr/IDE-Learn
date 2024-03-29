package gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.osgi.util.NLS;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import gr.alexc.idelearn.ui.classanalysis.parser.ClassChecker;
import gr.alexc.idelearn.ui.classanalysis.parser.ClassEntity;
import gr.alexc.idelearn.ui.classanalysis.parser.ConstructorMethod;
import gr.alexc.idelearn.ui.classanalysis.parser.Method;
import gr.alexc.idelearn.ui.messages.Messages;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MethodCallInsideConstructor extends AbstractSubRequirement {

	@JsonProperty("main_class_id")
	private ClassRequirement mainClass;

	@JsonProperty("constructor_method")
	private ConstructorRequirement constructor;

	@JsonProperty("call_method")
	private MethodRequirement callMethod;

	@JsonProperty("call_method_class_name")
	private String callMethodClassName;

	private String getArgumentsString() {
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
//        return "Constructor \"" + getArgumentsString() + "\" must have a method call to ...";
		return NLS.bind(Messages.reqConstructorCallMethod,
				new Object[] { mainClass.getName(), Messages.getMethodParametersString(constructor.getParameters()),
						Messages.getMethodSignature(callMethod), callMethodClassName });
	}

	@Override
	public boolean checkRequirement(ClassEntity classEntity) {

		for (ConstructorMethod m : classEntity.getConstructors()) {
			if (constructor.checkMethod(m)) {
				MethodCallVisitor visitor = new MethodCallVisitor();
				m.getBlockStmt().accept(visitor, null);
				List<Method> methodsFound = visitor.getMethodsFound();
				for (Method method : methodsFound) {
					if (callMethod.checkMethod(method)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	class MethodCallVisitor extends VoidVisitorAdapter<Void> {
		private final List<Method> methodsFound;

		public MethodCallVisitor() {
			super();
			methodsFound = new ArrayList<>();
		}

		public List<Method> getMethodsFound() {
			return methodsFound;
		}

		@Override
		public void visit(MethodCallExpr n, Void arg) {

			if (n.resolve().getClassName().equals(callMethodClassName)) {
				Optional<MethodDeclaration> methodDeclarationOptional = n.resolve().toAst();
				if (methodDeclarationOptional.isPresent()) {
					MethodDeclaration declaration = methodDeclarationOptional.get();
					methodsFound.add(ClassChecker.getMethodFromMethodDeclaration(declaration));
				}
			}

			// Don't forget to call super, it may find more method calls inside the
			// arguments of this method call, for example.
			super.visit(n, arg);
		}
	}

	public ClassRequirement getMainClass() {
		return mainClass;
	}

	public void setMainClass(ClassRequirement mainClass) {
		this.mainClass = mainClass;
	}

	public ConstructorRequirement getConstructor() {
		return constructor;
	}

	public void setConstructor(ConstructorRequirement constructor) {
		this.constructor = constructor;
	}

	public MethodRequirement getCallMethod() {
		return callMethod;
	}

	public void setCallMethod(MethodRequirement callMethod) {
		this.callMethod = callMethod;
	}

	public String getCallMethodClassName() {
		return callMethodClassName;
	}

	public void setCallMethodClassName(String callMethodClassName) {
		this.callMethodClassName = callMethodClassName;
	}
	
	
}
