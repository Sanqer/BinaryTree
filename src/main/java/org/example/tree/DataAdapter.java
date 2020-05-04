package org.example.tree;

import org.example.model.Person;

import java.util.List;

public interface DataAdapter {
    boolean addOrUpdate(Person person);
    boolean delete(int inn);
    Person get(int inn);
    List<Person> get();
}
