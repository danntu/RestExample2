package com.danntu.controller;

import com.danntu.model.User;
import com.danntu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/statusInfo/{status}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<List<User>> listInfoByStatus(@PathVariable("status") String status) {
        List<User> list = userService.listInfoByStatus("", status.toLowerCase());
        if (list.size() == 0) {
            return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<User>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/userAdd/", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        String id = "{\"id\":\"" + userService.createUser(user) + "\"}";
        return new ResponseEntity<String>(id, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/chngSts/", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<User> changeSts(@RequestBody User user) {
        User rUser = (User) userService.changeStatus(user.getId(), user.getStatusid());
        if (rUser == null) {
            return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<User>(rUser, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<User> getUserById(@PathVariable("id") String id) {
        User user = (User) userService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
}

