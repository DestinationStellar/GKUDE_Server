package com.tsingle.gkude_server;

import com.tsingle.gkude_server.entity.User;
import com.tsingle.gkude_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class GkudeServerApplicationTests {
    private final UserService service;
    @Test
    void UserDatabaseTest() {
        service.deleteAll();
        service.save(new User("Tom", "123456"));
        service.save(new User("Jack", "123456"));
        service.save(new User("Rose", "123456"));

        Optional<User> user = service.findByUserName("Jack");
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(user.get().getPassword(), "123456");

        user.get().setUsername("Jackson");
        service.save(user.get());

        user = service.findById(2L);
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(user.get().getUsername(), "Jackson");

        List<User> userList = service.findAll();
        Assertions.assertEquals(userList.size(), 3);

        service.delete(3L);

        userList = service.findAll();
        Assertions.assertEquals(userList.size(), 2);
    }

}
