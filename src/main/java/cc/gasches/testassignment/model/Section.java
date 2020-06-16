package cc.gasches.testassignment.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GeologicalClass> geologicalClasses;

    public void addGeologicalClass(GeologicalClass geoClass) {
        if (geologicalClasses == null) {
            geologicalClasses = new ArrayList<>();
        }
        geologicalClasses.add(geoClass);
        geoClass.setSection(this);
    }

    public void removeGeologicalClass(GeologicalClass geoClass) {
        if (geologicalClasses != null) {
            geologicalClasses.remove(geoClass);
            geoClass.setSection(null);
        }
    }
}
