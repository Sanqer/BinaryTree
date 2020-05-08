package org.example.tree;

import org.example.Main;
import org.example.filesave.*;
import org.example.model.Person;
import org.example.utils.FileManagerFactory;
import org.example.utils.SerializerFactory;
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
            FileManagerFactory.getFileManager().setSaver(new EmptySaver());
            FileManagerFactory.getFileManager().setLoader(new EmptyLoader());
            db = TreeAdapter.getInstance();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void addOrUpdateTest() {
        List<Person> pers = new ArrayList<>();
        assertEquals(0, db.get().size());
        pers.add(new Person("name2", 11, true, "1345", 555));
        pers.add(new Person("name1", 5, true, "134", 565));
        pers.add(new Person("name22", 15, false, "13", 567));
        pers.add(new Person("name34", 85, true, "1", 568));
        //pers.add(new Person("name344", 11, true, "1345", 555));
        for (Person item : pers) {
            db.addOrUpdate(item);
        }

        if (db instanceof TreeAdapter) {
            TreeAdapter dbTree = (TreeAdapter)db;
            FileManagerFactory.getFileManager().setSaver(new FileSaver());
            FileManagerFactory.getFileManager().save(dbTree.getTree());
            FileManagerFactory.getFileManager().setSaver(new EmptySaver());

            List<Person> newPers = db.get();
            assertEquals(pers.size(), newPers.size());
            assertArrayEquals(pers.toArray(), newPers.toArray());
            boolean deleted = db.delete(500);
            assertFalse(deleted);
            System.out.println(db.get());
            deleted = db.delete(555);
            assertTrue(deleted);
            System.out.println(db.get());

            assertNull(db.get(555));
            assertEquals(pers.get(1), db.get(565));
            System.out.println(db.get(565));
        }
    }

    @Test
    public void findTest() {
        db.addOrUpdate(new Person("", 1, true, "1", 568));
        assertNotNull(db.get(568));
    }

    @Test
    public  void deleteTest() {
        db.addOrUpdate(new Person("", 1, true, "1", 568));
        db.addOrUpdate(new Person("", 1, true, "1", 570));
        db.addOrUpdate(new Person("", 1, true, "1", 1000));
        db.addOrUpdate(new Person("", 1, true, "1", 1001));
        List<Person> list = db.get();
        for(Person item : list) {
            db.delete(item.getInn());
        }
        assertEquals(0, db.get().size());
        assertTrue(((TreeAdapter) db).getTree().isEmpty());
        db.addOrUpdate(new Person());
        assertFalse(((TreeAdapter)db).getTree().isEmpty());
        assertEquals(new Person(), db.get(0));
    }

    @Test
    public void FactoryTest() {
        FileManager manager = FileManagerFactory.getFileManager();
        String className = manager.getClass().getName();
        String simpName = manager.getClass().getSimpleName();

        Serializer ser = SerializerFactory.getSerializer();
        String serName = ser.getClass().getSimpleName();
    }

    @After
    public void endAll() { Main.stopServer(); }

}