package model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Вспомогательный Класс нашей модели
 * @autor Svytoq
 * @version 1.0
 */
public class Coordinates implements Serializable {
    private Double x;
    private Long y;

    public Coordinates(Double x, Long y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates() {
    }

    public Double getX() {
        return x;
    }

    public Long getY() {
        return y;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Long y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Objects.equals(x, that.x) && Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
