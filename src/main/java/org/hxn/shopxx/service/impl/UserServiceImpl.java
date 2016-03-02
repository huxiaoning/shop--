package org.hxn.shopxx.service.impl;

import org.hxn.shopxx.dao.UserMapper;
import org.hxn.shopxx.entity.User;
import org.hxn.shopxx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public User getUserById(int userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

}
