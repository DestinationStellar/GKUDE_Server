package com.tsingle.gkude_server.dao;

import com.tsingle.gkude_server.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProblemMapper extends JpaRepository<Problem, Long>, JpaSpecificationExecutor<Problem> {
    Optional<Problem> findProblemByQid(Integer qId);
}
