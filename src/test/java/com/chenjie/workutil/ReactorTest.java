package com.chenjie.workutil;

import com.chenjie.workutil.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	UserMapper userMapper;
	@Test
	public void findAll() {
		// 不传参数，会打印sql日志
		System.out.println(userMapper.findAll());
	}

	@Test
	public void findById() {
		// 传参数，不会打印sql日志
		System.out.println(userMapper.findById(1));
	}

}
