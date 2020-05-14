package org.binarytree.tree;

import org.binarytree.Main;
import org.binarytree.filesave.EmptyLoader;
import org.binarytree.filesave.EmptySaver;
import org.binarytree.model.Person;
import org.binarytree.tree.avltree.AvlTree;
import org.binarytree.tree.rbtree.RedBlackTree;
import org.binarytree.tree.sympletree.SimpleTree;
import org.binarytree.utils.FileManagerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class TreeContainerTest {
    private static Logger log = LoggerFactory.getLogger(TreeAdapterTest.class.getSimpleName());
    private List<Integer> sizes;
    private List<String> testNames;
    private TestUnit testTimes;
    private int opPerTest;
    private TreeAdapter db;

    @Before
    public void setUp() throws Exception {
        try {
            Main.main(null);
            FileManagerFactory.getFileManager().setSaver(new EmptySaver());
            FileManagerFactory.getFileManager().setLoader(new EmptyLoader());
            db = TreeAdapter.getInstance();
            opPerTest = 100000;
            testTimes = new TestUnit();
            sizes = TestUtils.getSizes();
            sizes = sizes.stream().filter(i -> i < 1000000 && i > 100).collect(Collectors.toList());
            testNames = TestUtils.getTestNames();
            ((TreeAdapter)db).setTree(FileManagerFactory.getFileManager().load());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            //System.out.println(ex.getMessage());
        }

    }

    @Test
    public void benchmarkAvlTree() {
        testTimes.clearAll();
        for (int size : sizes) {
            testTimes.addZeroes();
            for (String testName : testNames) {
                //System.out.println("AvlTree - " + size + " " + testName);
                performOnEmpty(new AvlTree<>(), size, testName, opPerTest);
            }
        }
        printTimes("AvlTree");
    }

    @Test
    public void benchmarkRedBlackTree() {
        testTimes.clearAll();
        for (int size : sizes) {
            testTimes.addZeroes();
            for (String testName : testNames) {
                //System.out.println("RedBlackTree - " + size + " " + testName);
                performOnEmpty(new RedBlackTree<>(), size, testName, opPerTest);
            }
        }
        printTimes("RedBlackTree");
    }

    @Test
    public void benchmarkSimpleTree() {
        testTimes.clearAll();
        for (int size : sizes) {
            testTimes.addZeroes();
            for (String testName : testNames) {
                if (size < 10000000)  {
                    if (size > 1000) {
                        if (testName.equalsIgnoreCase("sorted") || testName.equalsIgnoreCase("reverseSorted")) {
                            continue;
                        }
                    }
                    //System.out.println("SimpleTree - " + size + " " + testName);
                    performOnEmpty(new SimpleTree<>(), size, testName, opPerTest);
                }
            }
        }
        printTimes("SimpleTree");
    }

    private void printTimes(String treeName) {
        System.out.println(treeName + " - per " + opPerTest + " operations per test");
        System.out.print("\t\t\t\t\t");
        for (Integer size : sizes) {
            System.out.print(size + "\t");
        }
        System.out.println();
        System.out.print("random data ADD\t\t");
        for (int i = 0; i < testTimes.addTimes.size(); ++i) {
            System.out.print(testTimes.addTimes.get(i).get(false) + "\t");
        }
        System.out.println();
        System.out.print("random data FIND\t");
        for (int i = 0; i < testTimes.findTimes.size(); ++i) {
            System.out.print(testTimes.findTimes.get(i).get(false) + "\t");
        }
        System.out.println();
        System.out.print("random data DELETE\t");
        for (int i = 0; i < testTimes.deleteTimes.size(); ++i) {
            System.out.print(testTimes.deleteTimes.get(i).get(false) + "\t");
        }

        System.out.println();
        System.out.print("sorted data ADD\t\t");
        for (int i = 0; i < testTimes.addTimes.size(); ++i) {
            System.out.print(testTimes.addTimes.get(i).get(true) + "\t");
        }
        System.out.println();
        System.out.print("sorted data FIND\t");
        for (int i = 0; i < testTimes.findTimes.size(); ++i) {
            System.out.print(testTimes.findTimes.get(i).get(true) + "\t");
        }
        System.out.println();
        System.out.print("sorted data DELETE\t");
        for (int i = 0; i < testTimes.deleteTimes.size(); ++i) {
            System.out.print(testTimes.deleteTimes.get(i).get(true) + "\t");
        }
        System.out.println();
    }

    private void performOnEmpty(TreeContainer<Person> tree, int size, String testName, int testCounts) {
        db.setTree(tree);
        List<Person> testList = TestUtils.getTestList(getClass(), ((Integer)size).toString(), testName);
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
        int lastInd = testTimes.getLastInd();
        boolean sorted = false;
        if (testName.equalsIgnoreCase("sorted") || testName.equalsIgnoreCase("reverseSorted")) {
            sorted = true;
        }
        testTimes.addTimes.get(lastInd).add(addTime, testCounts, sorted);
        testTimes.findTimes.get(lastInd).add(findTime, testCounts, sorted);
        testTimes.deleteTimes.get(lastInd).add(deleteTime, testCounts, sorted);

        /*String onceAddTime = String.format("%.2f", (double)addTime / 1000000);
        String onceFindTime = String.format("%.2f", (double)findTime / 1000000);
        String onceDeleteTime = String.format("%.2f", (double)deleteTime / 1000000);
        String onceFillUpTime = String.format("%.2f", (double)fillUpTime / 1000000);
        System.out.println("find - " + onceFindTime);
        System.out.println("add - " + onceAddTime);
        System.out.println("delete - " + onceDeleteTime);
        System.out.println("fill up - " + onceFillUpTime);*/
    }

    @After
    public void tearDown() throws Exception {
        Main.stopServer();
    }

    private class TestUnit {
        private List<TimeUnit> addTimes;
        private List<TimeUnit> findTimes;
        private List<TimeUnit> deleteTimes;

        public TestUnit() {
            addTimes = new ArrayList<>();
            findTimes = new ArrayList<>();
            deleteTimes = new ArrayList<>();
        }

        public void clearAll() {
            addTimes.clear();
            findTimes.clear();
            deleteTimes.clear();
        }

        public void addZeroes() {
            addTimes.add(new TimeUnit());
            findTimes.add(new TimeUnit());
            deleteTimes.add(new TimeUnit());
        }

        public int getLastInd() {
            return addTimes.size() - 1;
        }

        public class TimeUnit {
            private double time;
            private double sortedTime;
            private int testCounts;
            private int sortedTestCounts;

            public TimeUnit() {
                time = 0;
                testCounts = 0;
                sortedTime = 0;
                sortedTestCounts = 0;
            }

            public void add(double time, int counts, boolean sorted) {
                if (sorted) {
                    this.sortedTime += time;
                    this.sortedTestCounts += counts;
                } else {
                    this.time += time;
                    testCounts += counts;
                }
            }

            public String get(boolean sorted) {
                if (sorted) {
                    int boost = sortedTestCounts / opPerTest;
                    return String.format("%.2f", sortedTime / boost / 1000000);
                } else {
                    int boost = testCounts / opPerTest;
                    return String.format("%.2f", time / boost / 1000000);
                }
            }
        }
    }
}