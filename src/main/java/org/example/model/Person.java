package org.example.model;

public class Person implements Comparable<Person>
{
    private String name;
    private int age;
    private boolean sex;
    private String phoneNumber;
    private long inn;

    public Person() {}

    public Person(String name, int age, boolean sex, String phoneNumber, long inn) {
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

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
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
        long res = this.inn - o.inn;
        if (res > 0) {
            return 1;
        } else if (res < 0) {
            return -1;
        }
        return 0;
    }
}
