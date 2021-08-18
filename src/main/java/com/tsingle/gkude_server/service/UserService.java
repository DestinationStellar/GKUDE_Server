package com.tsingle.gkude_server.service;

import com.tsingle.gkude_server.dao.UserMapper;
import com.tsingle.gkude_server.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserMapper mapper;

    public boolean save(User user)
    {
       mapper.save(user);
       return mapper.existsById(user.getId());
    }

    public boolean delete(Long id)
    {
        mapper.deleteById(id);
        return !mapper.existsById(id);
    }

    public boolean deleteAll() {
        mapper.deleteAll();
        return mapper.count() == 0L;
    }

    public Optional<User> findById(Long id)
    {
        return mapper.findById(id);
    }

    public Optional<User> findByUserName(String username)
    {
        return mapper.findByUsername(username);
    }

    public List<User> findAll()
    {
        return mapper.findAll();
    }
}
