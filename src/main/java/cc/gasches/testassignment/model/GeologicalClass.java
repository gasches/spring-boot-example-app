package cc.gasches.testassignment.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(exclude = "section")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "geological_classes")
public class GeologicalClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    private Section section;

    public GeologicalClass(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
