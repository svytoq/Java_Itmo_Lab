package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс нашей модели
 * @autor Svytoq
 * @version 1.0
 */
public class Product implements Comparable<Product>, Serializable {
        private int id;
        private String name;
        private Coordinates coordinates;
        private LocalDateTime creationDate;
        private Double price;
        private String partNumber;
        private double manufactureCost;
        private UnitOfMeasure unitOfMeasure;
        private Person owner;
        private String loginP;
        public Product(int id, String name, Coordinates coordinates, LocalDateTime creationDate, Double price, String partNumber, double manufactureCost, UnitOfMeasure unitOfMeasure, Person owner, String loginP) {
                this.id = id;
                this.name = name;
                this.coordinates = coordinates;
                this.creationDate = creationDate;
                this.price = price;
                this.partNumber = partNumber;
                this.manufactureCost = manufactureCost;
                this.unitOfMeasure = unitOfMeasure;
                this.owner = owner;
                this.loginP = loginP;
        }

        public Product() {
        }

        public int getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public Coordinates getCoordinates() {
                return coordinates;
        }

        public LocalDateTime getCreationDate() {
                return creationDate;
        }

        public Double getPrice() {
                return price;
        }

        public String getPartNumber() {
                return partNumber;
        }

        public double getManufactureCost() {
                return manufactureCost;
        }

        public UnitOfMeasure getUnitOfMeasure() {
                return unitOfMeasure;
        }

        public Person getOwner() {
                return owner;
        }

        public void setId(int id) {
                this.id = id;
        }

        public void setName(String name) {
                this.name = name;
        }

        public void setCoordinates(Coordinates coordinates) {
                this.coordinates = coordinates;
        }

        public void setCreationDate(LocalDateTime creationDate) {
                this.creationDate = creationDate;
        }

        public void setPrice(Double price) {
                this.price = price;
        }

        public void setPartNumber(String partNumber) {
                this.partNumber = partNumber;
        }

        public void setManufactureCost(double manufactureCost) {
                this.manufactureCost = manufactureCost;
        }

        public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
                this.unitOfMeasure = unitOfMeasure;
        }

        public void setOwner(Person owner) {
                this.owner = owner;
        }

        public String getLoginP() {
                return loginP;
        }

        public void setLoginP(String loginP) {
                this.loginP = loginP;
        }

        @Override
        public String toString() {
                return "Product{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", coordinates=" + coordinates.toString() +
                        ", creationDate=" + creationDate +
                        ", price=" + price +
                        ", partNumber='" + partNumber + '\'' +
                        ", manufactureCost=" + manufactureCost +
                        ", unitOfMeasure=" + unitOfMeasure +
                        ", owner=" + owner.toString() +
                        ", login=" + loginP.toString() +
                        '}';
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Product product = (Product) o;
                return id == product.id && Double.compare(product.manufactureCost, manufactureCost) == 0 && Objects.equals(name, product.name) && Objects.equals(coordinates, product.coordinates) && Objects.equals(creationDate, product.creationDate) && Objects.equals(price, product.price) && Objects.equals(partNumber, product.partNumber) && unitOfMeasure == product.unitOfMeasure && Objects.equals(owner, product.owner);
        }

        @Override
        public int hashCode() {
                return Objects.hash(id, name, coordinates, creationDate, price, partNumber, manufactureCost, unitOfMeasure, owner);
        }

        @Override
        public int compareTo(Product o) {
                return name.compareTo(o.getName()) ;
        }
}
