package com.danntu.service;

import com.danntu.doa.UserDao;
import com.danntu.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public String createUser(User user) {
        return userDao.createUser(user);
    }

    @Override
    public User getUserById(String id) {
        return userDao.getUserById(id);
    }

    @Override
    public User changeStatus(String id, int newStatus) {
        return userDao.changeStatus(id,newStatus);
    }

    @Override
    public List<User> listInfoByStatus(String id, String status) {
        return userDao.listInfoByStatus(id,status);
    }
}
