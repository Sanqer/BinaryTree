package org.example.tree;

import org.example.model.Person;
import org.example.utils.Factory;

import java.util.List;

public class TreeAdapter implements DataAdapter
{
    private TreeContainer<Person> tree;
    private static TreeAdapter instance;

    public static TreeAdapter GetInstance() {
        if (instance == null) {
            instance = new TreeAdapter();
        }
        return instance;
    }

    private TreeAdapter() {
        tree = Factory.getTree();
    }

    @Override
    public boolean addOrUpdate(Person person) {
        //try find in tree, then Add or (Delete and Add)
        return false;
    }

    @Override
    public boolean delete(int inn) {
        Person hollowPerson = new Person();
        hollowPerson.setInn(inn);
        return tree.delete(hollowPerson);
    }

    @Override
    public Person get(int inn) {
        Person hollowPerson = new Person();
        hollowPerson.setInn(inn);
        return tree.find(hollowPerson);
    }

    @Override
    public List<Person> get() {
        //tree.getAll
        return null;
    }
}
