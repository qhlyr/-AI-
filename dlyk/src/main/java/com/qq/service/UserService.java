package com.qq.service;

import com.qq.model.dto.UserDto;
import com.qq.model.po.TUser;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
	PageInfo<TUser> list(Integer pageNum, Integer pageSize);

	TUser getUserInfo(Long id);

	UserDto getUserDetail(Long id);

	boolean addUser(TUser user);

	boolean updateUser(TUser user);

	boolean deleteUser(Long id);

	boolean deleteUserBatch(Long[] ids);

	List<TUser> getAllUser();
}
