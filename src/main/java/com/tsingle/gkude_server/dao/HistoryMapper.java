package com.tsingle.gkude_server.dao;

import com.tsingle.gkude_server.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryMapper extends JpaRepository<History, Long>, JpaSpecificationExecutor<History> {
}
