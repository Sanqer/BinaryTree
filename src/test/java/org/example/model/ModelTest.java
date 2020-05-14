package org.example.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ModelTest {

    @Test
    public void personTest() {
        Person person = new Person("name", 1, Sex.MALE, "777777", 123);
        assertEquals("name", person.getName());
        assertEquals(1, person.getAge());
        assertEquals(Sex.MALE, person.getSex());
        assertEquals("777777", person.getPhoneNumber());
        assertEquals(123, person.getInn());

        person.setSex(Sex.FEMALE);
        assertEquals(Sex.FEMALE, person.getSex());
        person.setName("name1");
        assertEquals("name1", person.getName());
        person.setAge(11);
        assertEquals(11, person.getAge());
        person.setPhoneNumber("1");
        assertEquals("1", person.getPhoneNumber());
        person.setInn(111);
        assertEquals(111, person.getInn());
    }

    @Test
    public void answerTest() {
        Answer ans = new Answer("OK", null);
        assertEquals("OK", ans.getStatus());
        assertNull(ans.getItems());

        List<Person> people = new ArrayList<Person>() {{add(new Person("1", 1, Sex.MALE, "11", 1));}};
        ans.setItems(people);
        assertArrayEquals(people.toArray(), ans.getItems().toArray());
        ans.setStatus("NOT_OK");
        assertEquals("NOT_OK", ans.getStatus());
    }
}