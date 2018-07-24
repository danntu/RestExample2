package com.danntu.service;

import com.danntu.model.User;

import java.util.List;

public interface UserService {
    public String createUser(User user);
    public User getUserById(String id);
    public User changeStatus(String id, int newStatus);
    public List<User> listInfoByStatus(String id, String status);
}
