package gr.alexc.idelearn.ui.classanalysis.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Modifier {
    private String name;

    public static List<Modifier> getModifiersFromModifiers(List<com.github.javaparser.ast.Modifier> modifiersList) {
        List<Modifier> modifiers = new ArrayList<>();
        for (com.github.javaparser.ast.Modifier modifier : modifiersList) {
            modifiers.add(new Modifier(modifier.getKeyword().name()));
        }
        return modifiers;
    }
}
