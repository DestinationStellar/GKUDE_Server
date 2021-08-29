package com.tsingle.gkude_server.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EdukgEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NonNull
    private String uri;

    @Column
    @NonNull
    private String label;

    @Column
    @NonNull
    private String category;

    @Column
    private String description;

    @Column
    private String course;

    @Column
    private boolean visited;

    @Column
    @Lob
    private String relationStore;

    @Column
    @Lob
    private String propertyStore;

    @Column
    @Lob
    private String problemStore;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EdukgEntity edukgEntity = (EdukgEntity) o;
        return Objects.equals(uri, edukgEntity.uri);
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }

}
