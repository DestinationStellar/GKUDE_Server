package com.tsingle.gkude_server.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "wrong_problem")
@Getter
@Setter
@NoArgsConstructor
public class WrongProblem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private User user;

    @JoinColumn(name = "problem_id")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Problem problem;
}
