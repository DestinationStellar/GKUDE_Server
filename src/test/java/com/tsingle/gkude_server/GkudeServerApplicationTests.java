package com.tsingle.gkude_server;

import com.tsingle.gkude_server.dao.UserMapper;
import com.tsingle.gkude_server.entity.User;
import com.tsingle.gkude_server.service.EdukgService;
import com.tsingle.gkude_server.utils.EdukgResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class GkudeServerApplicationTests {
    private final UserMapper mapper;
    @Test
    void userDatabaseTest() {
        mapper.deleteAll();
        mapper.save(new User("Tom", "123456"));
        mapper.save(new User("Harry", "123456"));
        mapper.save(new User("Lily", "123456"));

        Optional<User> user = mapper.findByUsername("Harry");
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(user.get().getPassword(), "123456");

        user.get().setUsername("Hermione");
        mapper.save(user.get());

        user = mapper.findById(2L);
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(user.get().getUsername(), "Hermione");

        List<User> userList = mapper.findAll();
        Assertions.assertEquals(userList.size(), 3);

        mapper.deleteById(3L);

        userList = mapper.findAll();
        Assertions.assertEquals(userList.size(), 2);
    }

    private final EdukgService service;
    @Test
    void edukgClientTest() {
        System.out.println(service.getId());
    }

}
