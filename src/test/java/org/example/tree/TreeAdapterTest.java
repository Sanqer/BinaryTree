package org.example.tree;

import com.google.gson.reflect.TypeToken;
import org.example.Main;
import org.example.filesave.FileManager;
import org.example.filesave.Serializer;
import org.example.model.Person;
import org.example.tree.avltree.AvlTree;
import org.example.utils.Factory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TreeAdapterTest {

    private DataAdapter db;
    @Before
    public void init() {
        try {
            Main.main(null);
            db = Factory.getDataAdapter();
        } catch (Exception ex) {
            //ignore?
        }
    }

    @Test
    public void addOrUpdateTest() {
        List<Person> pers = new ArrayList<>();
        pers.add(new Person("name2", 11, true, "1345", 566));
        pers.add(new Person("name1", 11, true, "1345", 565));
        pers.add(new Person("name22", 11, true, "1345", 567));
        pers.add(new Person("name34", 11, true, "1345", 568));
        pers.add(new Person("name344", 11, true, "1345", 566));
        for (Person item : pers) {
            db.addOrUpdate(item);
        }

        if (db instanceof TreeAdapter) {
            String str = Factory.getSerializer().Serialize(((TreeAdapter) db).getTree());
            //str = "";
            AvlTree<Person> backtree = Factory.getSerializer().Deserialize(str, new TypeToken<AvlTree<Person>>() {
            }.getType());

            Person check = backtree.find(pers.get(0));
            Person check2 = db.get(566);
            assertEquals(check.getInn(), check2.getInn());
            boolean saved = Factory.getFileManager().save(backtree);
            assertTrue(saved);
        }
    }

    @Test
    public void FactoryTest() {
        FileManager manager = Factory.getFileManager();
        String className = manager.getClass().getName();
        String simpName = manager.getClass().getSimpleName();

        Serializer ser = Factory.getSerializer();
        String serName = ser.getClass().getSimpleName();
    }

    @After
    public void endAll() { Main.stopServer(); }

}