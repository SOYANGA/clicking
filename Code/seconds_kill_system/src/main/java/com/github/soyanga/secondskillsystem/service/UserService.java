package com.github.soyanga.secondskillsystem.service;

import com.github.soyanga.secondskillsystem.dao.UserDao;

import com.github.soyanga.secondskillsystem.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: seconds_kill_system
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-05-14 21:33
 * @Version 1.0
 */
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getById(int id) {
        return userDao.getById(id);
    }

    @Transactional
    public boolean tx() {
        User u1 = new User();
        u1.setId(2);
        u1.setName("2222");
        userDao.insert(u1);

        User u2 = new User();
        u2.setId(1);
        u2.setName("1111");
        userDao.insert(u2);
        return true;
    }
}
