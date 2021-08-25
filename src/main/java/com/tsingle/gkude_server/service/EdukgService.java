package com.tsingle.gkude_server.service;


import com.tsingle.gkude_server.httpclient.EdukgClient;
import com.tsingle.gkude_server.utils.EdukgResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EdukgService {
    private final EdukgClient edukgClient;
    private String id;

    public String getId() {
        Map<String, Object> info = new HashMap<>();
        info.put("phone", "18801356535");
        info.put("password", "gkude2021");
        id = edukgClient.getId(info).getId();
        return id;
    }
}