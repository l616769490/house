package com.tecode.house.azxl.bean;

import scala.Serializable;

import java.util.Objects;

public class MkValue implements Serializable {

    private String id;
    private int city;
    private int value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MkValue mkValue = (MkValue) o;
        return city == mkValue.city &&
                value == mkValue.value &&
                Objects.equals(id, mkValue.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, city, value);
    }

    @Override
    public String toString() {
        return "MkValue{" +
                "id='" + id + '\'' +
                ", city=" + city +
                ", value=" + value +
                '}';
    }

    public MkValue(String id, int city, int value) {
        this.id = id;
        this.city = city;
        this.value = value;
    }

    public MkValue() {
    }
}
