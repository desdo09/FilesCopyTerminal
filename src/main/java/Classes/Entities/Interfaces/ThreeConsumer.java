package Classes.Entities.Interfaces;

import java.util.Objects;
import java.util.function.Function;

/**
 * Created by SM David on 07/10/2018.
 */

public interface ThreeConsumer <T,V,Z> {

    void accept(T param1, V param2, Z param3);
}
