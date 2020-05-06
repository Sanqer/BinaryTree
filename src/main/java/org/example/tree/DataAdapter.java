package org.example.tree;

import org.example.model.Person;
import java.util.List;

public interface DataAdapter {
    boolean addOrUpdate(Person person);
    boolean delete(long inn);
    Person get(long inn);
    List<Person> get();
}
