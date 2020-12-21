package gr.alexc.idelearn.ui.classanalysis.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConstructorMethod extends Method {
    private Type type = null;
}
