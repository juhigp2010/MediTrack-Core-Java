package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.util.Validator;

public abstract class Person extends MedicalEntity {
    private String name;
    private int age;

    public Person(String id, String name, int age) {
        super(id);
        Validator.validateName(name);
        Validator.validateAge(age);
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Validator.validateName(name);
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        Validator.validateAge(age);
        this.age = age;
    }
}
