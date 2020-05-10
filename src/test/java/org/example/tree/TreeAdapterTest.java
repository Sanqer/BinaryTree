package org.example.tree;

import org.example.Main;
import org.example.filesave.*;
import org.example.model.Person;
import org.example.model.Sex;
import org.example.utils.FileManagerFactory;
import org.example.utils.SerializerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class TreeAdapterTest {

    private TreeAdapter db;
    @Before
    public void init() {
        try {
            Main.main(null);
            FileManagerFactory.getFileManager().setSaver(new EmptySaver());
            FileManagerFactory.getFileManager().setLoader(new EmptyLoader());
            db = TreeAdapter.getInstance();
            ((TreeAdapter)db).setTree(FileManagerFactory.getFileManager().load());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
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
        List<Person> list = fillTree(1000);
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
        List<Person> list = fillTree(bound);
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
        fillTree(1000);
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
        List<Person> list = fillTree(10);
        manager.save(db.getTree());
        db.setTree(manager.load());
        assertArrayEquals(list.toArray(), db.get().toArray());

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

    private List<Person> makePersonList(int bound) {
        List<Person> list = new ArrayList<>();
        Sex[] sexes = Sex.values();
        Random rand = new Random();
        for (int i = 0; i < bound; ++i) {
            Person tempPer = new Person();
            long inn = rand.nextLong();
            //if (inn < 0) inn *= -1;
            tempPer.setInn(inn);

            tempPer.setAge(rand.nextInt(80));
            tempPer.setName("name" + rand.nextInt());
            tempPer.setPhoneNumber("+7" + rand.nextInt(1000000));
            tempPer.setSex(sexes[rand.nextInt(sexes.length)]);
            list.add(tempPer);
        }
        Set<Person> set = new TreeSet<>();
        list = list.stream().filter(set::add).collect(Collectors.toList());
        Collections.shuffle(list);
        return list;
    }

    private List<Person> fillTree(int bound) {
        List<Person> list = makePersonList(bound);
        for (Person pers : list) {
            db.addOrUpdate(pers);
        }
        Collections.sort(list);
        return list;
    }
}