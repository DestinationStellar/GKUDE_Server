package com.tsingle.gkude_server.service;

import com.tsingle.gkude_server.dao.EdukgEntityMapper;
import com.tsingle.gkude_server.dao.FavoriteMapper;
import com.tsingle.gkude_server.dao.HistoryMapper;
import com.tsingle.gkude_server.dao.UserMapper;
import com.tsingle.gkude_server.entity.EdukgEntity;
import com.tsingle.gkude_server.entity.Favorite;
import com.tsingle.gkude_server.entity.History;
import com.tsingle.gkude_server.entity.User;
import com.tsingle.gkude_server.utils.JsonResponse;
import com.tsingle.gkude_server.utils.JwtUtil;
import com.tsingle.gkude_server.utils.MD5Util;
import com.tsingle.gkude_server.utils.ResponseUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserMapper userMapper;
    private final EdukgEntityMapper edukgEntityMapper;
    private final FavoriteMapper favoriteMapper;
    private final HistoryMapper historyMapper;
    @NonNull
    private HttpServletRequest request;
    @NonNull
    private HttpServletResponse response;

    public JsonResponse<?> login(String username, String password) {
        Optional<User> optionalUser = userMapper.findByUsername(username);
        User user;
        if (optionalUser.isEmpty()) {
            user = userMapper.save(new User(username, MD5Util.getMD5(password)));
        } else {
            if (!MD5Util.getMD5(password).equals(optionalUser.get().getPassword())) {
                return new JsonResponse<>(ResponseUtil.BAD_REQUEST.getStatus(),
                        ResponseUtil.BAD_REQUEST.getMessage(), "Wrong password.");
            }
            user = optionalUser.get();
        }

        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtUtil.keyUserId, user.getId());
            long tokenFailureTime = 30 * 24 * 60 * 60L;

            String token = JwtUtil.createJWT(claims, UUID.randomUUID().toString(), "GKUDE_Server", "User", tokenFailureTime);
            return new JsonResponse<>(ResponseUtil.OK.getStatus(), ResponseUtil.OK.getMessage(), token);

        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResponse<String>(ResponseUtil.INTERNAL_SERVER_ERROR.getStatus(),
                    ResponseUtil.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    public JsonResponse<?> updateUsername(String newUsername) {
        Optional<User> optionalUser = userMapper.findById((Long) request.getAttribute("userId"));
        if (optionalUser.isEmpty()) {
            return new JsonResponse<>(ResponseUtil.BAD_REQUEST.getStatus(),
                    ResponseUtil.BAD_REQUEST.getMessage(), "User not found.");
        }
        User user = optionalUser.get();
        try {
            user.setUsername(newUsername);
            userMapper.save(user);
        } catch (DataIntegrityViolationException e) {
            return new JsonResponse<>(ResponseUtil.BAD_REQUEST.getStatus(),
                    ResponseUtil.BAD_REQUEST.getMessage(), "User already exists.");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResponse<String>(ResponseUtil.INTERNAL_SERVER_ERROR.getStatus(),
                    ResponseUtil.INTERNAL_SERVER_ERROR.getMessage());
        }
        return new JsonResponse<String>(ResponseUtil.OK.getStatus(), ResponseUtil.OK.getMessage());
    }

    public JsonResponse<?> updatePassword(String oldPassword, String newPassword) {
        Optional<User> optionalUser = userMapper.findById((Long) request.getAttribute("userId"));
        if (optionalUser.isEmpty()) {
            return new JsonResponse<>(ResponseUtil.BAD_REQUEST.getStatus(),
                    ResponseUtil.BAD_REQUEST.getMessage(), "User not found.");
        }
        User user = optionalUser.get();
        if (!MD5Util.getMD5(oldPassword).equals(user.getPassword())) {
            return new JsonResponse<>(ResponseUtil.BAD_REQUEST.getStatus(),
                    ResponseUtil.BAD_REQUEST.getMessage(), "Wrong password.");
        }
        user.setPassword(newPassword);
        userMapper.save(user);
        return new JsonResponse<String>(ResponseUtil.OK.getStatus(), ResponseUtil.OK.getMessage());
    }

    public JsonResponse<?> addFavorite(EdukgEntity entity) {
        Optional<User> optionalUser = userMapper.findById((Long) request.getAttribute("userId"));
        if (optionalUser.isEmpty()) {
            return new JsonResponse<>(ResponseUtil.BAD_REQUEST.getStatus(),
                    ResponseUtil.BAD_REQUEST.getMessage(), "User not found.");
        }
        User user = optionalUser.get();
        Optional<EdukgEntity> optionalEdukgEntity = edukgEntityMapper.findEdukgEntityByUri(entity.getUri());
        if (optionalEdukgEntity.isPresent()) {
            entity = optionalEdukgEntity.get();
            for (Favorite f: user.getFavorites()) {
                if (f.getEdukgEntity().equals(entity)) {
                    return new JsonResponse<>(ResponseUtil.BAD_REQUEST.getStatus(),
                            ResponseUtil.BAD_REQUEST.getMessage(), "Already add favorite.");
                }
            }
        } else {
            entity = edukgEntityMapper.save(entity);
        }
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setEdukgEntity(entity);
        user.getFavorites().add(favorite);

        favoriteMapper.save(favorite);
        userMapper.save(user);
        return new JsonResponse<String>(ResponseUtil.OK.getStatus(), ResponseUtil.OK.getMessage());
    }

    public JsonResponse<?> cancelFavorite(EdukgEntity entity) {
        Optional<User> optionalUser = userMapper.findById((Long) request.getAttribute("userId"));
        if (optionalUser.isEmpty()) {
            return new JsonResponse<>(ResponseUtil.BAD_REQUEST.getStatus(),
                    ResponseUtil.BAD_REQUEST.getMessage(), "User not found.");
        }
        Optional<EdukgEntity> optionalEdukgEntity = edukgEntityMapper.findEdukgEntityByUri(entity.getUri());
        if (optionalEdukgEntity.isEmpty()) {
            return new JsonResponse<>(ResponseUtil.BAD_REQUEST.getStatus(),
                    ResponseUtil.BAD_REQUEST.getMessage(), "Entity doesn't exist.");
        }
        User user = optionalUser.get();
        EdukgEntity edukgEntity = optionalEdukgEntity.get();
        for (Favorite f: user.getFavorites()) {
            if (f.getEdukgEntity().equals(edukgEntity)) {
                user.getFavorites().remove(f);
                favoriteMapper.delete(f);
                break;
            }
        }
        userMapper.save(user);
        return new JsonResponse<String>(ResponseUtil.OK.getStatus(), ResponseUtil.OK.getMessage());
    }

    public JsonResponse<?> getFavorites() {
        Optional<User> optionalUser = userMapper.findById((Long) request.getAttribute("userId"));
        if (optionalUser.isEmpty()) {
            return new JsonResponse<>(ResponseUtil.BAD_REQUEST.getStatus(),
                    ResponseUtil.BAD_REQUEST.getMessage(), "User not found.");
        }
        return new JsonResponse<>(ResponseUtil.OK.getStatus(), ResponseUtil.OK.getMessage(), optionalUser.get().getFavorites());
    }

    public JsonResponse<?> addHistory(EdukgEntity entity) {
        Optional<User> optionalUser = userMapper.findById((Long) request.getAttribute("userId"));
        if (optionalUser.isEmpty()) {
            return new JsonResponse<>(ResponseUtil.BAD_REQUEST.getStatus(),
                    ResponseUtil.BAD_REQUEST.getMessage(), "User not found.");
        }
        History history = new History();
        Optional<EdukgEntity> optionalEdukgEntity = edukgEntityMapper.findEdukgEntityByUri(entity.getUri());
        if (optionalEdukgEntity.isEmpty()) {
            entity = edukgEntityMapper.save(entity);
        }
        history.setEdukgEntity(entity);
        User user = optionalUser.get();
        history.setUser(user);
        user.getHistories().add(history);

        historyMapper.save(history);
        userMapper.save(user);
        return new JsonResponse<String>(ResponseUtil.OK.getStatus(), ResponseUtil.OK.getMessage());
    }

    public JsonResponse<?> getHistory() {
        Optional<User> optionalUser = userMapper.findById((Long) request.getAttribute("userId"));
        if (optionalUser.isEmpty()) {
            return new JsonResponse<>(ResponseUtil.BAD_REQUEST.getStatus(),
                    ResponseUtil.BAD_REQUEST.getMessage(), "User not found.");
        }
        return new JsonResponse<>(ResponseUtil.OK.getStatus(), ResponseUtil.OK.getMessage(), optionalUser.get().getHistories());
    }

}
