package July24;

import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface LibraryFilter {
    boolean filter(Book book);
}
