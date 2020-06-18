package cc.gasches.geodata.model;

import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "export_queue")
public class ExportItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Lob
    private Blob file;

    @Enumerated(EnumType.STRING)
    private Status status = Status.IN_PROGRESS;

    private String message;

    public enum Status {
        IN_PROGRESS,
        DONE,
        ERROR
    }
}
