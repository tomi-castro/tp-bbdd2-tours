package unlp.info.bd2.model;

import java.util.List;

import jakarta.persistence.*;
@Entity
public class Padre1 {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@OneToMany(cascade = CascadeType.REMOVE)
private List<Hijo1> hijos = new java.util.ArrayList<Hijo1>();


public Long getId() {
    return id;
}
public void addChild(Hijo1 hijo) {
    this.hijos.add(hijo);
}
public void removeChild(Hijo1 hijo) {
    this.hijos.remove(hijo);
}
}