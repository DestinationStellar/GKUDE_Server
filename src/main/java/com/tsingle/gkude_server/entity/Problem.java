package com.tsingle.gkude_server.entity;

import lombok.*;

import javax.persistence.*;

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
}
