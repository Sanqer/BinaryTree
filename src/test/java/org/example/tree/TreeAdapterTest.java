package org.example.tree;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.example.Main;
import org.example.filesave.*;
import org.example.model.Person;
import org.example.model.Sex;
import org.example.tree.avltree.AvlTree;
import org.example.tree.rbtree.RedBlackTree;
import org.example.tree.sympletree.SimpleTree;
import org.example.utils.Common;
import org.example.utils.FileManagerFactory;
import org.example.utils.SerializerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class TreeAdapterTest {
    private static Logger log = LoggerFactory.getLogger(TreeAdapterTest.class.getSimpleName());
    private TreeAdapter db;
    private List<Integer> sizes;
    private List<String> testNames;


    @Before
    public void init() {
        try {
            Main.main(null);
            FileManagerFactory.getFileManager().setSaver(new EmptySaver());
            FileManagerFactory.getFileManager().setLoader(new EmptyLoader());
            db = TreeAdapter.getInstance();
            ((TreeAdapter)db).setTree(FileManagerFactory.getFileManager().load());
            fillTestCasesLists();
            //createAllSamples();
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
        List<Person> list = fillTree(100);
        manager.save(db.getTree());
        db.setTree(manager.load());
        assertArrayEquals(list.toArray(), db.get().toArray());
        db.setTree(manager.load());
        assertArrayEquals(list.toArray(), db.get().toArray());
        db.setTree(manager.load());
        db.get(list.get(0).getInn());
        assertArrayEquals(list.toArray(), db.get().toArray());
        System.out.println(db.get());
        assertArrayEquals(list.toArray(), db.get().toArray());

        manager.setSaver(new EmptySaver());
        while(list.size() > 0) {
            boolean deleted = db.delete(list.get(0).getInn());
            list.remove(0);
            assertTrue(deleted);
            assertArrayEquals(list.toArray(), db.get().toArray());
        }
    }

    @Test
    public void benchmark() {
        for (int size : sizes) {
            for (String testName : testNames) {
                System.out.println("AvlTree - " + size + " " + testName);
                performOnEmpty(new AvlTree<>(), size, testName, 10000);
                System.out.println("RedBlackTree - " + size + " " + testName);
                performOnEmpty(new RedBlackTree<>(), size, testName, 10000);

                if (size < 100000)  {
                    System.out.println("SimpleTree - " + size + " " + testName);
                    performOnEmpty(new SimpleTree<>(), size, testName, 10);
                }
            }
        }
    }

    private void performOnEmpty(TreeContainer<Person> tree, int size, String testName, int testCounts) {
        db.setTree(tree);
        List<Person> testList = getTestList(((Integer)size).toString(), testName);
        long begin;
        long end;
        long findTime = 0;
        long addTime = 0;
        long deleteTime = 0;
        long fillUpTime = 0;

        begin = System.nanoTime();
        for (Person person : testList) {
            db.getTree().add(person);
        }
        end = System.nanoTime();
        fillUpTime = end - begin;

        int minStart = Math.max(testList.size() - testCounts, 0);
        for (int i = minStart; i < minStart + testCounts; ++i) {
            Person personToTest = testList.get(i % testList.size());

            begin = System.nanoTime();
            Person foundPerson = db.getTree().find(personToTest);
            assertEquals(personToTest, foundPerson);
            end = System.nanoTime();
            findTime += (end - begin);

            begin = System.nanoTime();
            boolean deleted = db.getTree().delete(personToTest);
            end = System.nanoTime();
            deleteTime += (end - begin);
            assertTrue(deleted);

            begin = System.nanoTime();
            boolean added = db.getTree().add(personToTest);
            end = System.nanoTime();
            addTime += (end - begin);
            assertTrue(added);
        }

        String onceAddTime = String.format("%.2f", (double)addTime / 1000000);
        String onceFindTime = String.format("%.2f", (double)findTime / 1000000);
        String onceDeleteTime = String.format("%.2f", (double)deleteTime / 1000000);
        String onceFillUpTime = String.format("%.2f", (double)fillUpTime / 1000000);
        System.out.println("find - " + onceFindTime);
        System.out.println("add - " + onceAddTime);
        System.out.println("delete - " + onceDeleteTime);
        System.out.println("fill up - " + onceFillUpTime);
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
            long inn = i + 1;
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

    private boolean createFile(String fileName, List<Person> persons) throws IOException{
        File file = new File("src/test/resources/" + fileName);
        //InputStream inStream = getClass().getResourceAsStream("/" + fileName);
        if (file.exists()) {
            return false;
        }
        String strPersons = Common.getPrettyGson().toJson(persons);
        OutputStream outStream = new FileOutputStream("src/test/resources/" + fileName);
        outStream.write(strPersons.getBytes(StandardCharsets.UTF_8));
        outStream.close();
        return true;
    }

    private void createSamples(int size) throws IOException {
        int count = 10;
        String sizePath = ((Integer)size).toString();
        for (int i = 0; i < count; ++i) {
            String indName = ((Integer)i).toString();
            List<Person> persons = makePersonList(size);
            createFile(sizePath + "/" + indName, persons);
        }

        List<Person> persons = makePersonList(size);
        Collections.sort(persons);
        createFile(sizePath + "/sorted", persons);

        persons = makePersonList(size);
        persons.sort(Comparator.reverseOrder());
        createFile(sizePath + "/reverseSorted", persons);
        //getClass().getResourceAsStream
    }

    private void createAllSamples() throws IOException {
        createSamples(1000);
        createSamples(10000);
        createSamples(100000);
        createSamples(1000000);
    }

    private void fillTestCasesLists() {
        if (sizes == null) {
            sizes = Arrays.asList(1000, 10000, 100000, 1000000);
        }
        if (testNames == null) {
            testNames = new ArrayList<>();
            for (int i = 0; i < 10; ++i) {
                testNames.add(((Integer)i).toString());
            }
            testNames.add("reverseSorted");
            testNames.add("sorted");
        }
    }

    private List<Person> fillTree(int bound) {
        List<Person> list = makePersonList(bound);
        for (Person pers : list) {
            db.addOrUpdate(pers);
        }
        Collections.sort(list);
        return list;
    }

    private List<Person> getTestList(String size, String testName) {
        InputStream inStream = getClass().getResourceAsStream("/" + size + "/" + testName);
        try {
            String inp = IOUtils.toString(inStream, StandardCharsets.UTF_8);
            return Common.getPrettyGson().fromJson(inp, new TypeToken<List<Person>>() {}.getType());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return new ArrayList<>();
    }

    private List<Person> fillTree(String size, String testName) {
        List<Person> list = getTestList(size, testName);
        for (Person pers : list) {
            db.addOrUpdate(pers);
        }
        return list;
    }
}