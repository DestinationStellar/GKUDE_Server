package com.tsingle.gkude_server.service;

import com.tsingle.gkude_server.dao.*;
import com.tsingle.gkude_server.entity.*;
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
    private final ProblemMapper problemMapper;
    private final WrongProblemMapper wrongProblemMapper;
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
            long tokenFailureTime = 30 * 24 * 60 * 60 * 1000L;

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
        user.setPassword(MD5Util.getMD5(newPassword));
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
                    return new JsonResponse<>(ResponseUtil.ACCEPTED.getStatus(),
                            ResponseUtil.ACCEPTED.getMessage(), "Already add favorite.");
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
            return new JsonResponse<>(ResponseUtil.ACCEPTED.getStatus(),
                    ResponseUtil.ACCEPTED.getMessage(), "Entity doesn't exist.");
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
                    ResponseUtil.BAD_REQUEST.getMessage(), null);
        }
        List<EdukgEntity> list = new ArrayList<>();
        for(Favorite f: optionalUser.get().getFavorites()) {
            list.add(f.getEdukgEntity());
        }
        return new JsonResponse<>(ResponseUtil.OK.getStatus(), ResponseUtil.OK.getMessage(), list);
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
        } else {
            entity = optionalEdukgEntity.get();
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
                    ResponseUtil.BAD_REQUEST.getMessage(), null);
        }
        List<EdukgEntity> list = new ArrayList<>();
        for (History h: optionalUser.get().getHistories()) {
            list.add(h.getEdukgEntity());
        }
        return new JsonResponse<>(ResponseUtil.OK.getStatus(), ResponseUtil.OK.getMessage(), list);
    }

    public JsonResponse<?> addWrongProbelm(Problem problem) {
        Optional<User> optionalUser = userMapper.findById((Long) request.getAttribute("userId"));
        if (optionalUser.isEmpty()) {
            return new JsonResponse<>(ResponseUtil.BAD_REQUEST.getStatus(),
                    ResponseUtil.BAD_REQUEST.getMessage(), "User not found.");
        }
        WrongProblem wrongProblem = new WrongProblem();
        Optional<Problem> optionalProblem = problemMapper.findProblemByQid(problem.getQid());
        if (optionalProblem.isEmpty()) {
            problem = problemMapper.save(problem);
        } else {
            problem = optionalProblem.get();
        }
        wrongProblem.setProblem(problem);
        User user = optionalUser.get();
        wrongProblem.setUser(user);
        user.getWrongProblems().add(wrongProblem);

        wrongProblemMapper.save(wrongProblem);
        userMapper.save(user);
        return new JsonResponse<>(ResponseUtil.OK.getStatus(), ResponseUtil.OK.getMessage());
    }

    public JsonResponse<?> getWrongProblem() {
        Optional<User> optionalUser = userMapper.findById((Long) request.getAttribute("userId"));
        if (optionalUser.isEmpty()) {
            return new JsonResponse<>(ResponseUtil.BAD_REQUEST.getStatus(),
                    ResponseUtil.BAD_REQUEST.getMessage(), null);
        }
        List<Problem> list = new ArrayList<>();
        for (WrongProblem w: optionalUser.get().getWrongProblems()) {
            list.add(w.getProblem());
        }
        return new JsonResponse<>(ResponseUtil.OK.getStatus(), ResponseUtil.OK.getMessage(), list);
    }
}
