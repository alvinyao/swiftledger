package com.higgschain.trust.contract.mock;

import com.alibaba.fastjson.JSONObject;

/**
 * The type Person.
 */
public class Person {
    private String name;
    private int age;
    private Colors color;
    private JSONObject state;

    /**
     * Instantiates a new Person.
     */
    public Person() {}

    /**
     * Instantiates a new Person.
     *
     * @param name the name
     * @param age  the age
     */
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets age.
     *
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets age.
     *
     * @param age the age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gets color.
     *
     * @return the color
     */
    public Colors getColor() {
        return color;
    }

    /**
     * Sets color.
     *
     * @param color the color
     */
    public void setColor(Colors color) {
        this.color = color;
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public JSONObject getState() {
        return state;
    }

    /**
     * Sets state.
     *
     * @param state the state
     */
    public void setState(JSONObject state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return String.format("name:%s, age:%d", name, age);
    }
}
