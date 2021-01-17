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
import gr.alexc.idelearn.ui.classanalysis.parser.Method;
import gr.alexc.idelearn.ui.messages.Messages;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MethodCallInsideMethod extends AbstractSubRequirement {

	@JsonProperty("main_class_id")
	private ClassRequirement mainClass;

	@JsonProperty("method")
	private MethodRequirement method;

	@JsonProperty("call_method")
	private MethodRequirement callMethod;

	@JsonProperty("call_method_class_name")
	private String callMethodClassName;

	@JsonProperty("is_call_method_class_super_class")
	private Boolean isCallMethodClassSuperClass = false;

	@Override
	public String getDescription() {
//        return "Method \"" + method.getName() + "\" must have a method call to ...";
		if (isCallMethodClassSuperClass) {
			return NLS.bind(Messages.reqMethodCallMethod,
					new Object[] { Messages.getMethodSignature(method), mainClass.getName(),
							Messages.getMethodSignature(callMethod), Messages.reqSuperClassesText, callMethodClassName });
		} else {
			return NLS.bind(Messages.reqMethodCallMethod,
					new Object[] { Messages.getMethodSignature(method), mainClass.getName(),
							Messages.getMethodSignature(callMethod), Messages.reqClassesText, callMethodClassName });
		}

	}

	@Override
	public boolean checkRequirement(ClassEntity classEntity) {

		for (Method m : classEntity.getMethods()) {
			if (method.checkMethod(m)) {
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
			// Found a method call
//            System.out.println(n.getScope() + " - " + n.getName());
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
}
