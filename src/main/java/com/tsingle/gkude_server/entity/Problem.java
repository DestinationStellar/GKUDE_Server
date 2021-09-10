package com.tsingle.gkude_server.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NonNull
    private Integer qid;

    @Lob
    @NonNull
    private String qBody;

    @Lob
    @NonNull
    private String qAnswer;

    @Column
    @NonNull
    private String course;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Problem problem = (Problem) o;
        return Objects.equals(qid, problem.qid);
    }

    @Override
    public int hashCode() {
        return qid.hashCode();
    }
}
