package org.binarytree.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.Objects;

public class Person implements Comparable<Person>
{
    private String name;
    private int age;
    @JacksonXmlProperty
    private Sex sex;
    private String phoneNumber;
    private long inn;

    public Person() {}

    public Person(String name, int age, Sex sex, String phoneNumber, long inn) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.inn = inn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getInn() {
        return inn;
    }

    public void setInn(long inn) {
        this.inn = inn;
    }

    @Override
    public int compareTo(Person o) {
        return Long.compare(this.inn, o.inn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age &&
                sex == person.sex &&
                inn == person.inn &&
                Objects.equals(name, person.name) &&
                Objects.equals(phoneNumber, person.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, sex, phoneNumber, inn);
    }

    @Override
    public String toString() {
        return "Person{" +
                "inn=" + inn +
                '}';
    }
}
