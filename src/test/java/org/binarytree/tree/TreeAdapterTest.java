package org.binarytree.tree;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.binarytree.Main;
import org.binarytree.filesave.*;
import org.binarytree.model.Person;
import org.binarytree.model.Sex;
import org.binarytree.utils.FileManagerFactory;
import org.binarytree.utils.SerializerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.Assert.*;

public class TreeAdapterTest {
    private static Logger log = LoggerFactory.getLogger(TreeAdapterTest.class.getSimpleName());
    private TreeAdapter db;
    private List<Integer> sizes;
    private List<String> testNames;
    private int opPerTest;
    //private TestUnit testTimes;
    private Map<String, List<Person>> cash;


    @Before
    public void init() {
        try {
            Main.main(null);
            FileManagerFactory.getFileManager().setSaver(new EmptySaver());
            FileManagerFactory.getFileManager().setLoader(new EmptyLoader());
            db = TreeAdapter.getInstance();
            ((TreeAdapter)db).setTree(FileManagerFactory.getFileManager().load());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            //System.out.println(ex.getMessage());
        }
    }

    @Test
    public void addOrUpdateTest() {
        List<Person> pers = new ArrayList<>();
        assertEquals(0, db.get().size());
        pers.add(new Person("name2", 11, Sex.MALE, "1345", 555));
        pers.add(new Person("name1", 5, Sex.MALE, "134", 565));
        pers.add(new Person("name22", 15, Sex.FEMALE, "13", 567));
        pers.add(new Person("name34", 85, Sex.MALE, "1", 568));
        //pers.add(new Person("name344", 11, true, "1345", 555));
        for (Person item : pers) {
            db.addOrUpdate(item);
        }

        if (db instanceof TreeAdapter) {
            TreeAdapter dbTree = (TreeAdapter)db;
            FileManagerFactory.getFileManager().setSaver(new FileSaver());
            //FileManagerFactory.getFileManager().save(dbTree.getTree());
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
    public void insertFindTest() {
        List<Person> list = TestUtils.fillTree(db, getClass(), "1000", "0");
        Collections.sort(list);
        List<Person> treeColl = db.get();
        assertArrayEquals(list.toArray(), treeColl.toArray());
        for (Person pers : list) {
            Person treePerson = db.get(pers.getInn());
            assertEquals(pers, treePerson);
        }
    }

    @Test
    public void deleteTest() {
        int bound = 1000;
        List<Person> list = TestUtils.fillTree(db, getClass(), "1000", "0");
        Collections.sort(list);
        int deleteBound = 100;
        List<Person> sortedList = new ArrayList<>(list);
        Collections.shuffle(list);
        for (int i = 0; i < deleteBound; ++i) {
            Person personToDel = list.remove(list.size() - 1);
            int ind = Collections.binarySearch(sortedList, personToDel);
            sortedList.remove(ind);
            boolean deleted = db.delete(personToDel.getInn());
            assertTrue(deleted);
            assertNull(db.get(personToDel.getInn()));
            assertArrayEquals(sortedList.toArray(), db.get().toArray());
        }
    }

    @Test
    public void fullDeleteTest() {
        TestUtils.fillTree(db, getClass(), "1000", "0");
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
    public void saveLoadTest() {
        FileManager manager = FileManagerFactory.getFileManager();
        manager.setSaver(new FileSaver());
        manager.setLoader(new FileLoader());
        List<Person> list = TestUtils.fillTree(db, getClass(), "100", "0");
        Collections.sort(list);
        manager.save(db.getTree());
        db.setTree(manager.load());
        assertArrayEquals(list.toArray(), db.get().toArray());
        System.out.println(db.get());

        manager.setSaver(new EmptySaver());
        while(list.size() > 0) {
            boolean deleted = db.delete(list.get(0).getInn());
            list.remove(0);
            assertTrue(deleted);
            assertArrayEquals(list.toArray(), db.get().toArray());
        }
    }

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