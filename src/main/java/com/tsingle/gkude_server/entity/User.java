package com.tsingle.gkude_server.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NonNull
    private String username;
    @NonNull
    private String password;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Favorite> favorites;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<History> histories;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
