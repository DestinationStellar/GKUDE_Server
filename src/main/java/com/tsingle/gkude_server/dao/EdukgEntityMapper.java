package com.tsingle.gkude_server.dao;

import com.tsingle.gkude_server.entity.EdukgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EdukgEntityMapper extends JpaRepository<EdukgEntity, Long>, JpaSpecificationExecutor<EdukgEntity> {
    Optional<EdukgEntity> findEdukgEntityByUri(String uri);
}
