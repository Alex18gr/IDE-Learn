package gr.alexc.idelearn.ui.classanalysis.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import lombok.Getter;
import lombok.Setter;

public class ClassEntity {

    private String className;
    private Set<ClassEntity> extendClasses;
    private Set<ClassEntity> implementTypes;
    private Set<ClassRelation> relations;
    private Boolean isInterface;
    private Boolean isAbstract;
    private Boolean isStatic;

    private List<String> extendClassesName;
    private List<String> implementClassesName;
    private List<Field> fields;
    private List<Method> methods;
    private List<ConstructorMethod> constructors;

    private ClassOrInterfaceDeclaration classDeclaration;

    public ClassEntity() {
        relations = new HashSet<>();
        this.extendClasses = new HashSet<>();
        this.implementTypes = new HashSet<>();
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.constructors = new ArrayList<>();
    }

    public ClassEntity(String className) {
        this.className = className;
        relations = new HashSet<>();
        this.extendClasses = new HashSet<>();
        this.implementTypes = new HashSet<>();
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.constructors = new ArrayList<>();
    }

    public void addRelationToClass(ClassEntity relatedClassEntity, ClassRelation.RelationType relationType) {
        this.relations.add(new ClassRelation(this, relatedClassEntity, relationType));

    }

    public void addExtendClass(ClassEntity classEntity) {
        this.extendClasses.add(classEntity);
    }

    public void addImplementType(ClassEntity classEntity) {
        this.implementTypes.add(classEntity);
    }

    public boolean hasA(String className) {
        for (ClassRelation relation : relations) {
            return relation.getRelatedClass().getClassName().equals(className) &&
                    relation.getRelationType() != ClassRelation.RelationType.MANY_TO_ONE;
        }
        return false;
    }

    public boolean hasA(ClassEntity classEntity) {
        for (ClassRelation relation : relations) {
            return relation.getRelatedClass().equals(classEntity) &&
                    relation.getRelationType() != ClassRelation.RelationType.MANY_TO_ONE;
        }
        return false;
    }

    public boolean extendsA(String className) {
        for (ClassEntity extendClass : extendClasses) {
            if (extendClass.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    public boolean extendsA(ClassEntity extendClassEntity) {
        for (ClassEntity extendClass : extendClasses) {
            if (extendClass.equals(extendClassEntity)) {
                return true;
            }
        }
        return false;
    }

    public boolean isA(String className) {
        for (ClassEntity extendClass : extendClasses) {
            if (extendClass.getClassName().equals(className)) {
                return true;
            } else {
                return isA(className, extendClass.getExtendClasses());
            }
        }
        return false;
    }

    private boolean isA(String className, Set<ClassEntity> classEntities) {
        for (ClassEntity extendClass : classEntities) {
            if (extendClass.getClassName().equals(className)) {
                return true;
            } else {
                return isA(className, extendClass.getExtendClasses());
            }

        }
        return false;
    }

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Set<ClassEntity> getExtendClasses() {
		return extendClasses;
	}

	public void setExtendClasses(Set<ClassEntity> extendClasses) {
		this.extendClasses = extendClasses;
	}

	public Set<ClassEntity> getImplementTypes() {
		return implementTypes;
	}

	public void setImplementTypes(Set<ClassEntity> implementTypes) {
		this.implementTypes = implementTypes;
	}

	public Set<ClassRelation> getRelations() {
		return relations;
	}

	public void setRelations(Set<ClassRelation> relations) {
		this.relations = relations;
	}

	public Boolean getIsInterface() {
		return isInterface;
	}

	public void setIsInterface(Boolean isInterface) {
		this.isInterface = isInterface;
	}

	public Boolean getIsAbstract() {
		return isAbstract;
	}

	public void setIsAbstract(Boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public Boolean getIsStatic() {
		return isStatic;
	}

	public void setIsStatic(Boolean isStatic) {
		this.isStatic = isStatic;
	}

	public List<String> getExtendClassesName() {
		return extendClassesName;
	}

	public void setExtendClassesName(List<String> extendClassesName) {
		this.extendClassesName = extendClassesName;
	}

	public List<String> getImplementClassesName() {
		return implementClassesName;
	}

	public void setImplementClassesName(List<String> implementClassesName) {
		this.implementClassesName = implementClassesName;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public List<Method> getMethods() {
		return methods;
	}

	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}

	public List<ConstructorMethod> getConstructors() {
		return constructors;
	}

	public void setConstructors(List<ConstructorMethod> constructors) {
		this.constructors = constructors;
	}

	public ClassOrInterfaceDeclaration getClassDeclaration() {
		return classDeclaration;
	}

	public void setClassDeclaration(ClassOrInterfaceDeclaration classDeclaration) {
		this.classDeclaration = classDeclaration;
	}
    
    

}
