package com.qq.mapper;

import com.qq.annotation.DataScope;
import com.qq.model.dto.UserDto;
import com.qq.model.po.TUser;
import com.qq.model.query.BaseQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 797799421
* @description 针对表【t_user(用户表)】的数据库操作Mapper
* @createDate 2024-06-14 18:11:10
* @Entity com.biluo.model.po.TUser
*/
@Mapper
public interface TUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(TUser record);

    int insertSelective(TUser record);

    TUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TUser record);

    int updateByPrimaryKey(TUser record);

	TUser selectByLoginAct(String username);

	@DataScope(tableAlias = "tu", tableField = "id")
	List<TUser> list(BaseQuery query);

	UserDto getUserDetail(Long id);

	int deleteByIds(Long[] ids);

	List<TUser> getAllUser(BaseQuery query);
}
