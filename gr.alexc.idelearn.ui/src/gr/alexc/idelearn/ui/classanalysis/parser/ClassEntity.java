package gr.alexc.idelearn.ui.classanalysis.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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

}
