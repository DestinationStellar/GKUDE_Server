package com.tsingle.gkude_server.service;

import com.tsingle.gkude_server.dao.UserMapper;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserMapper mapper;
    @NonNull
    private HttpServletRequest request;
    @NonNull
    private HttpServletResponse response;

    public JsonResponse login(String username, String password) {
        Optional<User> optionalUser = mapper.findByUsername(username);
        User user;
        if (optionalUser.isEmpty()) {
            user = mapper.save(new User(username, MD5Util.getMD5(password)));
        } else {
            if (!MD5Util.getMD5(password).equals(optionalUser.get().getPassword())) {
                return new JsonResponse(ResponseUtil.BAD_REQUEST.getStatus(),
                        ResponseUtil.BAD_REQUEST.getMessage(), "Wrong password.");
            }
            user = optionalUser.get();
        }

        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtUtil.keyUserId, user.getId());
            long tokenFailureTime = 30 * 24 * 60 * 60L;

            String token = JwtUtil.createJWT(claims, UUID.randomUUID().toString(), "GKUDE_Server", "User", tokenFailureTime);
            return new JsonResponse(ResponseUtil.OK.getStatus(), ResponseUtil.OK.getMessage(), token);

        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResponse(ResponseUtil.INTERNAL_SERVER_ERROR.getStatus(),
                    ResponseUtil.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    public JsonResponse updateUsername(String newUsername) {
        Optional<User> optionalUser = mapper.findById((Long) request.getAttribute("userId"));
        if (optionalUser.isEmpty()) {
            return new JsonResponse(ResponseUtil.BAD_REQUEST.getStatus(),
                    ResponseUtil.BAD_REQUEST.getMessage(), "User not found.");
        }
        User user = optionalUser.get();
        try {
            user.setUsername(newUsername);
            mapper.save(user);
        } catch (DataIntegrityViolationException e) {
            return new JsonResponse(ResponseUtil.BAD_REQUEST.getStatus(),
                    ResponseUtil.BAD_REQUEST.getMessage(), "User already exists.");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResponse(ResponseUtil.INTERNAL_SERVER_ERROR.getStatus(),
                    ResponseUtil.INTERNAL_SERVER_ERROR.getMessage());
        }
        return new JsonResponse(ResponseUtil.OK.getStatus(), ResponseUtil.OK.getMessage());
    }

    public JsonResponse updatePassword(String oldPassword, String newPassword) {
        Optional<User> optionalUser = mapper.findById((Long) request.getAttribute("userId"));
        if (optionalUser.isEmpty()) {
            return new JsonResponse(ResponseUtil.BAD_REQUEST.getStatus(),
                    ResponseUtil.BAD_REQUEST.getMessage(), "User not found.");
        }
        User user = optionalUser.get();
        if (!MD5Util.getMD5(oldPassword).equals(user.getPassword())) {
            return new JsonResponse(ResponseUtil.BAD_REQUEST.getStatus(),
                    ResponseUtil.BAD_REQUEST.getMessage(), "Wrong password.");
        }
        user.setPassword(newPassword);
        mapper.save(user);
        return new JsonResponse(ResponseUtil.OK.getStatus(), ResponseUtil.OK.getMessage());
    }
}
