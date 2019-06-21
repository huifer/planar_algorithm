package com.huifer.planar.aset.mappingalgo.sortpath;

import java.util.Objects;

/**
 * <p>Title : Vertex </p>
 * <p>Description : 顶点</p>
 *
 * @author huifer
 * @date 2018/11/08
 */
public class Vertex {

    public String name;
    public double weight =100000;


    public Vertex(String newName) {
        this.name = newName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vertex vertex = (Vertex) o;
        return Double.compare(vertex.weight, weight) == 0 &&
                Objects.equals(name, vertex.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, weight);
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                '}';
    }
}
