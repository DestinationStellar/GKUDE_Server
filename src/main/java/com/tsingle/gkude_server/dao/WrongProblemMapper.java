package com.tsingle.gkude_server.dao;

import com.tsingle.gkude_server.entity.WrongProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WrongProblemMapper extends JpaRepository<WrongProblem, Long>, JpaSpecificationExecutor<WrongProblem> {
}
