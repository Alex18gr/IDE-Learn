package gr.alexc.idelearn.ui.classanalysis.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;

public class ClassChecker {

	private final Map<String, ClassEntity> visitedClasses;

	public Map<String, ClassEntity> getVisitedClasses() {
		return visitedClasses;
	}

	public ClassChecker() {
		visitedClasses = new HashMap<>();
	}

	public void checkClasses(SourceRoot sourceRoot) throws IOException {
		sourceRoot.tryToParse();
		List<CompilationUnit> compilations = sourceRoot.getCompilationUnits();
		this.checkClasses(compilations);
	}

	public void checkClasses(List<CompilationUnit> compilations) {

		// check the classes from the compilation units
		List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarations = new ArrayList<>();
		VoidVisitor<List<ClassOrInterfaceDeclaration>> classVisitor = new ClassNamePrinter();
		for (CompilationUnit compilationUnit : compilations) {
			classVisitor.visit(compilationUnit, classOrInterfaceDeclarations);
		}
		
        // create the class entities
        this.createClassEntities(classOrInterfaceDeclarations);

		// check the classes relations
		this.checkClassRelations(classOrInterfaceDeclarations);

		this.checkClassContents(classOrInterfaceDeclarations);

		// print the related classes
//        for (String className : visitedClasses.keySet()) {
//            System.out.println("------------------------------------------------------------------");
//            ClassEntity entity = visitedClasses.get(className);
//
//            System.out.println("Class \"" + entity.getClassName() + "\" extend Classes:");
//            for (ClassEntity classEntity : entity.getExtendClasses()) {
//                System.out.println("\t" + classEntity.getClassName());
//            }
//
//            System.out.println("Class \"" + entity.getClassName() + "\" implements Interfaces:");
//            for (ClassEntity classEntity : entity.getImplementTypes()) {
//                System.out.println("\t" + classEntity.getClassName());
//            }
//
//            System.out.println("Class \"" + entity.getClassName() + "\" is related to classes:");
//            for (ClassRelation relation : entity.getRelations()) {
//                System.out.println("\t" + relation.getRelatedClass().getClassName() + " with relation " + relation.getRelationType());
//            }
//            System.out.println("------------------------------------------------------------------");
//        }

//        if (this.visitedClasses.get("LabClassroom") != null) {
//            System.out.println("Is LabClassroom a Classroom ? " + this.visitedClasses.get("LabClassroom").isA("Classroom"));
//        }
//        if (this.visitedClasses.get("LabClassroom") != null) {
//            System.out.println("Is LabClassroom a Student ? " + this.visitedClasses.get("LabClassroom").isA("Student"));
//        }
//        if (this.visitedClasses.get("OOPComputerLabClassroom") != null) {
//            System.out.println("Is LabClassroom a Student ? " + this.visitedClasses.get("OOPComputerLabClassroom").isA("Classroom"));
//        }
	}

	private void createClassEntities(List<ClassOrInterfaceDeclaration> declarations) {
		// create the map with the class entities
		for (ClassOrInterfaceDeclaration declaration : declarations) {
			ClassEntity classEntity = new ClassEntity();
			classEntity.setClassName(declaration.getNameAsString());
			classEntity.setClassDeclaration(declaration);
			classEntity.setIsInterface(declaration.isInterface());
			classEntity.setIsAbstract(declaration.isAbstract());
			this.visitedClasses.put(declaration.getNameAsString(), classEntity);
		}
	}

	private void checkClassContents(List<ClassOrInterfaceDeclaration> declarations) {
		for (ClassOrInterfaceDeclaration declaration : declarations) {
			String className = declaration.getNameAsString();
			ClassEntity classEntity = this.visitedClasses.get(className);
			if (classEntity != null) {
				classEntity.setFields(this.getFieldsFromFieldDeclarations(declaration.getFields()));
				classEntity.setMethods(this.getMethodsFromMethodDeclarations(declaration.getMethods()));
				classEntity.setExtendClassesName(this.getExtendClassesName(declaration.getExtendedTypes()));
				classEntity.setImplementClassesName(this.getImplementClassesName(declaration.getImplementedTypes()));
				classEntity.setConstructors(this.getConstructors(declaration.getConstructors()));
			}

		}
	}

	private List<ConstructorMethod> getConstructors(List<ConstructorDeclaration> constructors) {
		List<ConstructorMethod> constructorMethods = new ArrayList<>();
		for (ConstructorDeclaration c : constructors) {
            constructorMethods.add(getConstructorMethodFromConstructorMethodDeclaration(c));
		}
		return constructorMethods;
	}
	
    public static ConstructorMethod getConstructorMethodFromConstructorMethodDeclaration(ConstructorDeclaration declaration) {
        ConstructorMethod method = new ConstructorMethod();
        method.setModifiers(Modifier.getModifiersFromModifiers(declaration.getModifiers()));
        if (declaration.getBody() != null) {
            method.setBlockStmt(declaration.getBody());
        }
        method.setParameters(Parameter.getParametersFromParameters(declaration.getParameters()));
        return method;
    }

	private List<String> getImplementClassesName(NodeList<ClassOrInterfaceType> implementedTypes) {
		List<String> implementClassesName = new ArrayList<>();
		for (ClassOrInterfaceType type : implementedTypes) {
			implementClassesName.add(type.getNameAsString());
		}
		return implementClassesName;
	}

	private List<String> getExtendClassesName(NodeList<ClassOrInterfaceType> extendedTypes) {
		List<String> extendClassesName = new ArrayList<>();
		for (ClassOrInterfaceType type : extendedTypes) {
			extendClassesName.add(type.getNameAsString());
		}
		return extendClassesName;
	}

	private List<Method> getMethodsFromMethodDeclarations(List<MethodDeclaration> methodDeclarations) {
		List<Method> methods = new ArrayList<>();
		for (MethodDeclaration declaration : methodDeclarations) {
            methods.add(getMethodFromMethodDeclaration(declaration));
		}
		return methods;
	}
	
    public static Method getMethodFromMethodDeclaration(MethodDeclaration declaration) {
        Method method = new Method();
        method.setModifiers(Modifier.getModifiersFromModifiers(declaration.getModifiers()));
        method.setName(declaration.getNameAsString());
        method.setType(gr.alexc.idelearn.ui.classanalysis.parser.Type.getTypeFromClassOrInterfaceType(declaration.getType()));
        if (declaration.getBody().isPresent()) {
            method.setBlockStmt(declaration.getBody().get());
        }
        method.setParameters(Parameter.getParametersFromParameters(declaration.getParameters()));
        return method;
    }

	private List<Field> getFieldsFromFieldDeclarations(List<FieldDeclaration> declarations) {
		List<Field> fields = new ArrayList<>();
		for (FieldDeclaration declaration : declarations) {
			Field field = new Field();
			field.setModifiers(Modifier.getModifiersFromModifiers(declaration.getModifiers()));
			field.setName(declaration.getVariable(0).getNameAsString());
			field.setType(gr.alexc.idelearn.ui.classanalysis.parser.Type
					.getTypeFromClassOrInterfaceType(declaration.getElementType()));
			fields.add(field);
		}
		return fields;
	}

	private void checkClassRelations(List<ClassOrInterfaceDeclaration> declarations) {

		// find the relations between the classes
		for (ClassOrInterfaceDeclaration declaration : declarations) {

			ClassEntity currentClass = this.visitedClasses.get(declaration.getNameAsString());

			NodeList<ClassOrInterfaceType> extendTypes = declaration.getExtendedTypes();
			NodeList<ClassOrInterfaceType> implementTypes = declaration.getImplementedTypes();

			if (extendTypes.isNonEmpty()) {
				extendTypes.forEach(type -> {
					ClassEntity extendClassEntity = this.visitedClasses.get(type.getNameAsString());
					if (extendClassEntity != null) {
						currentClass.addExtendClass(extendClassEntity);
					}
				});
			}

			if (implementTypes.isNonEmpty()) {
//                System.out.println("Implement Types:");
				implementTypes.forEach(type -> {
					ClassEntity implementClassEntity = this.visitedClasses.get(type.getNameAsString());
					if (implementClassEntity != null) {
						currentClass.addImplementType(implementClassEntity);
					}
				});
			}

			declaration.getFields().forEach(fieldDeclaration -> {
//                System.out.println("Field Printed: " + fieldDeclaration.getElementType());
				if (fieldDeclaration.getElementType().isClassOrInterfaceType()) {
					String className = fieldDeclaration.getElementType().asClassOrInterfaceType().getName().asString();
//                    System.out.println("Field Printed: " + className);

					ClassEntity relatedClass = this.visitedClasses.get(className);

					if (relatedClass != null) {
						// if it is a known class we add it as a relation to the current checking class
						this.visitedClasses.get(declaration.getNameAsString()).addRelationToClass(relatedClass,
								ClassRelation.RelationType.ONE_TO_ONE);
					}

					if (fieldDeclaration.getElementType().asClassOrInterfaceType().getTypeArguments().isPresent()) {
						NodeList<Type> arguments = fieldDeclaration.getElementType().asClassOrInterfaceType()
								.getTypeArguments().get();
//                        for (Type a : arguments) {
//                            System.out.println("\tField Type Argument " + (arguments.indexOf(a) + 1) + ": " + a);
//                        }
						if (checkClassCollectionByName(className)) {
							relatedClass = this.visitedClasses
									.get(arguments.get(0).asClassOrInterfaceType().getNameAsString());
							if (relatedClass != null) {
								// if it is a known class we add it as a relation to the current checking class
								this.visitedClasses.get(declaration.getNameAsString()).addRelationToClass(relatedClass,
										ClassRelation.RelationType.ONE_TO_MANY);
								relatedClass.addRelationToClass(this.visitedClasses.get(declaration.getNameAsString()),
										ClassRelation.RelationType.MANY_TO_ONE);
							}
						}
					}
				} else {
//                    System.out.println("Field Printed: " + fieldDeclaration.getElementType());
				}
			});
		}
	}

	private Boolean checkClassCollectionByName(String name) {
		return name.equals("Iterable") || name.equals("Collection") || name.equals("List") || name.equals("ArrayList")
				|| name.equals("LinkedList") || name.equals("Vector") || name.equals("Stack") || name.equals("Queue")
				|| name.equals("Deque") || name.equals("ArrayDeque") || name.equals("PriorityQueue")
				|| name.equals("Set") || name.equals("SortedSet") || name.equals("TreeSet") || name.equals("HashSet")
				|| name.equals("LinkedHashSet");
	}

	private static class ClassNamePrinter extends VoidVisitorAdapter<List<ClassOrInterfaceDeclaration>> {
		@Override
		public void visit(ClassOrInterfaceDeclaration n, List<ClassOrInterfaceDeclaration> declarations) {
			super.visit(n, declarations);

//            System.out.println("Class Name Printed: " + n.getName());
			ClassEntity classEntity = new ClassEntity();
			classEntity.setClassName(n.getName().asString());
			classEntity.setClassDeclaration(n);
//			if (n.isInterface()) {
//				classEntity.setIsInterface(true);
//			}
//			if (n.isAbstract()) {
//				n.isAbstract();
//			}
//            visitedClasses.put(n.getNameAsString(), classEntity);
			declarations.add(n);

			NodeList<ClassOrInterfaceType> extendTypes = n.getExtendedTypes();
			NodeList<ClassOrInterfaceType> implementTypes = n.getImplementedTypes();

//            if (extendTypes.isNonEmpty()) {
////                System.out.println("Extend Types:");
//                extendTypes.forEach(type -> {
//                    System.out.println("\t" + (extendTypes.indexOf(type) + 1) + ": " + type.getName());
//                });
//            }

//            if (implementTypes.isNonEmpty()) {
////                System.out.println("Implement Types:");
//                implementTypes.forEach(type -> {
//                    System.out.println("\t" + (implementTypes.indexOf(type) + 1) + ": " + type.getName());
//                });
//            }

//            n.getTypeParameters().forEach((typeParameter -> {
//                System.out.println("Parameter Printed: " + typeParameter.getName());
//            }));
			n.getFields().forEach(fieldDeclaration -> {
//                System.out.println("Field Printed: " + fieldDeclaration.getElementType());
				if (fieldDeclaration.getElementType().isClassOrInterfaceType()) {
//                    System.out.println("Field Printed: " + fieldDeclaration.getElementType().asClassOrInterfaceType().getName());
					// if it is a known class we add it as a relation to
					if (fieldDeclaration.getElementType().asClassOrInterfaceType().getTypeArguments().isPresent()) {
						NodeList<Type> arguments = fieldDeclaration.getElementType().asClassOrInterfaceType()
								.getTypeArguments().get();
						for (Type a : arguments) {
//                            System.out.println("\tField Type Argument " + (arguments.indexOf(a) + 1) + ": " + a);
						}
					}
				} else {
//                    System.out.println("Field Printed: " + fieldDeclaration.getElementType());
				}
			});
//            System.out.println("-------------------------------------");
		}

	}
}
