package com.tsingle.gkude_server.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "edukg_entity")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
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
    @NonNull
    private String course;

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
