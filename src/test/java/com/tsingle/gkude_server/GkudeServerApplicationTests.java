package com.tsingle.gkude_server;

import com.tsingle.gkude_server.entity.User;
import com.tsingle.gkude_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class GkudeServerApplicationTests {
    private final UserService service;
    @Test
    void UserDatabaseTest() {
        service.deleteAll();

        service.save(new User(100L, "Tom", "123456"));
        service.save(new User(101L, "Jack", "123456"));
        service.save(new User(102L, "Rose", "123456"));

        User user = service.select(101L);
        Assertions.assertEquals(user.getUsername(), "Jack");

        service.save(new User(101L, "Jackson", "123456"));

        user = service.select(101L);
        Assertions.assertEquals(user.getUsername(), "Jackson");

        List<User> userList = service.selectAll();
        Assertions.assertEquals(userList.size(), 3);

        service.delete(102L);

        userList = service.selectAll();
        Assertions.assertEquals(userList.size(), 2);
    }

}
