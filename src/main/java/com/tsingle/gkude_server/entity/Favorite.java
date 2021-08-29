package com.tsingle.gkude_server.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private User user;

    @JoinColumn
    @ManyToOne
    private EdukgEntity edukgEntity;
}
