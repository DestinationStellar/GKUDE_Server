package com.tsingle.gkude_server.controller;

import com.tsingle.gkude_server.entity.EdukgEntity;
import com.tsingle.gkude_server.service.UserService;
import com.tsingle.gkude_server.utils.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService service;

    @PostMapping("login")
    public JsonResponse<?> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        return service.login(username, password);
    }

    @PostMapping("update/name")
    public JsonResponse<?> updateUsername(@RequestParam("newUsername") String newUsername) {
        return service.updateUsername(newUsername);
    }

    @PostMapping("update/password")
    public JsonResponse<?> updatePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
        return service.updatePassword(oldPassword, newPassword);
    }

    @PostMapping("favorite/add")
    public JsonResponse<?> addFavorite(@RequestParam("data") EdukgEntity entity) {
        return service.addFavorite(entity);
    }

    @PostMapping("favorite/cancel")
    public JsonResponse<?> cancelFavorite(@RequestParam("data") EdukgEntity entity) {
        return service.cancelFavorite(entity);
    }

    @GetMapping("favorite/get")
    public JsonResponse<?> getFavorites() {
        return service.getFavorites();
    }

    @PostMapping("history/add")
    public JsonResponse<?> addHistory(@RequestParam("data") EdukgEntity entity) {
        return service.addHistory(entity);
    }

    @GetMapping("history/get")
    public JsonResponse<?> getHistory() {
        return service.getHistory();
    }
}