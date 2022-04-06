package model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Вспомогательный Класс нашей модели
 * @autor Svytoq
 * @version 1.0
 */
public class Person implements Comparable<Person>, Serializable {
    private String name;
    private float weight;
    private Color eyeColor;
    private Location location;

    public Person(String name, float weight, Color eyeColor, Location location) {
        this.name = name;
        this.weight = weight;
        this.eyeColor = eyeColor;
        this.location = location;
    }

    public Person(){

    }
    public String getName() {
        return name;
    }

    public float getWeight() {
        return weight;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public Location getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", eyeColor=" + eyeColor +
                ", location=" + location.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Float.compare(person.weight, weight) == 0 && Objects.equals(name, person.name) && eyeColor == person.eyeColor && Objects.equals(location, person.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, weight, eyeColor, location);
    }

    @Override
    public int compareTo(Person o) {
        return name.compareTo(o.getName());
    }
}
