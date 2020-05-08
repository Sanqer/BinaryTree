package org.example.tree;

import org.example.model.Person;
import org.example.utils.FileManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TreeAdapter implements DataAdapter
{
    private static Logger log = LoggerFactory.getLogger(TreeAdapter.class.getSimpleName());
    private TreeContainer<Person> tree;
    private static TreeAdapter instance;

    public static TreeAdapter getInstance() {
        if (instance == null) {
            instance = new TreeAdapter();
        }
        return instance;
    }

    private TreeAdapter() {
        tree = FileManagerFactory.getFileManager().load();
    }

    @Override
    public boolean addOrUpdate(Person person) {
        Person foundPerson = tree.find(person);
        boolean added = true;
        boolean checkDel = true;
        boolean checkAdd;
        if (foundPerson != null) {
            checkDel = tree.delete(person);
            added = false;
        }
        checkAdd = tree.add(person);
        if (!checkAdd || !checkDel) {
            log.error("Ошибка добавления/обновления элемента {}", person);
        }
        FileManagerFactory.getFileManager().save(tree);
        return added;
    }

    @Override
    public boolean delete(long inn) {
        Person hollowPerson = new Person();
        hollowPerson.setInn(inn);
        boolean isDel = tree.delete(hollowPerson);
        FileManagerFactory.getFileManager().save(tree);
        return isDel;
    }

    @Override
    public Person get(long inn) {
        Person hollowPerson = new Person();
        hollowPerson.setInn(inn);
        return tree.find(hollowPerson);
    }

    @Override
    public List<Person> get() {
        return tree.getAll();
    }

    public TreeContainer<Person> getTree() {
        return tree;
    }

    public void setTree(TreeContainer<Person> tree) {
        this.tree = tree;
    }
}
