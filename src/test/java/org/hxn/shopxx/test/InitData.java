package org.hxn.shopxx.test;

import org.hxn.shopxx.entity.User;
import org.hxn.shopxx.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sckj.macb.query.IObjectQuery;
import com.sckj.macb.query.ObjectQueryImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class InitData {

	@Autowired
	private UserService userService;

	@Test
	public void testGet() {
		User user = userService.getUserById(1);
		System.out.println(user.getUserName());
	}
	
	@Test
	public void testMacb(){
		IObjectQuery query = new ObjectQueryImpl(User.class);
		String sql = query.id(1).sql();
		System.out.println(sql);
	}
}
