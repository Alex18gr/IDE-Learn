package gr.alexc.idelearn.ui.classanalysis.parser;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Field {
    private String name;
    private List<Modifier> modifiers;
    private Type type;
}
