package com.tsingle.gkude_server.service;

import com.tsingle.gkude_server.dao.UserMapper;
import com.tsingle.gkude_server.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserMapper mapper;

    public boolean save(User user)
    {
        Long id = user.getId();
        User currentUser = select(id);
        if(currentUser != null)
            return mapper.update(user) == 1;
        return mapper.insert(user) == 1;
    }

    public boolean delete(Long id)
    {
        return mapper.deleteById(id) == 1;
    }

    public boolean deleteAll() {
        return mapper.deleteAll() == 1;
    }

    public User select(Long id)
    {
        return mapper.selectById(id);
    }

    public List<User> selectAll()
    {
        return mapper.selectAll();
    }
}
