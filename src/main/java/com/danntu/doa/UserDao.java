package com.danntu.doa;

import com.danntu.model.User;

import java.util.List;

public interface UserDao {
    public String createUser(User user);
    public User getUserById(String id);
    public User changeStatus(String id, int newStatus);
    public List<User> listInfoByStatus(String id,String status);
}
