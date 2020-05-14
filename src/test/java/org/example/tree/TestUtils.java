package org.example.tree;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.example.model.Person;
import org.example.model.Sex;
import org.example.utils.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class TestUtils {
    private static Logger log = LoggerFactory.getLogger(TreeAdapterTest.class.getSimpleName());
    private static Map<String, List<Person>> cash;
    private static List<Integer> sizes;
    private static List<String> testNames;

    static {
        try {
            cash = new HashMap<>();
            //createAllSamples();
            fillTestCasesLists();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private static boolean createFile(String fileName, List<Person> persons) throws IOException {
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

    private static void createSamples(int size) throws IOException {
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

    private static void createAllSamples() throws IOException {
        createSamples(100);
        createSamples(1000);
        createSamples(10000);
        createSamples(100000);
        createSamples(1000000);
    }

    private static void fillTestCasesLists() {
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

    public static List<Person> makePersonList(int bound) {
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

    public static List<Person> fillTree(DataAdapter db, int bound) {
        List<Person> list = makePersonList(bound);
        for (Person pers : list) {
            db.addOrUpdate(pers);
        }
        Collections.sort(list);
        return list;
    }

    public static List<Person> getTestList(Class<?> resourceClazz, String size, String testName) {
        String key = size + "_" + testName;
        if (cash.containsKey(key)) {
            return cash.get(key);
        }
        InputStream inStream = resourceClazz.getResourceAsStream("/" + size + "/" + testName);
        try {
            String inp = IOUtils.toString(inStream, StandardCharsets.UTF_8);
            List<Person> list = Common.getPrettyGson().fromJson(inp, new TypeToken<List<Person>>() {}.getType());
            cash.put(key, list);
            return list;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return new ArrayList<>();
    }

    public static List<Person> fillTree(DataAdapter db, Class<?> resourceClazz, String size, String testName) {
        List<Person> list = getTestList(resourceClazz, size, testName);
        for (Person pers : list) {
            db.addOrUpdate(pers);
        }
        return list;
    }

    public static List<Integer> getSizes() {
        return sizes;
    }

    public static List<String> getTestNames() {
        return testNames;
    }
}
