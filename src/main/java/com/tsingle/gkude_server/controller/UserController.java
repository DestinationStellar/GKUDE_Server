package com.tsingle.gkude_server.controller;

import com.tsingle.gkude_server.entity.User;
import com.tsingle.gkude_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService service;

    @PostMapping("save")
    public boolean save(@RequestBody User user) {
        return service.save(user);
    }

    @GetMapping("delete")
    public boolean delete(@RequestParam Long id) {
        return service.delete(id);
    }

    @GetMapping("select")
    public User select(@RequestParam Long id) {
        return service.findById(id).orElse(null);
    }

    @GetMapping("selectAll")
    public List<User> selectAll() {
        return service.findAll();
    }
}