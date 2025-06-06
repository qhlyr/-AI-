package com.qq.mapper;

import com.qq.annotation.DataScope;
import com.qq.model.dto.ClueDto;
import com.qq.model.po.TClue;
import com.qq.model.query.BaseQuery;
import com.qq.model.result.EchartsData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 797799421
* @description 针对表【t_clue(线索表)】的数据库操作Mapper
* @createDate 2024-06-14 18:11:10
* @Entity com.biluo.model.po.TClue
*/
@Mapper
public interface TClueMapper {

    int deleteByPrimaryKey(Long id);

    int insert(TClue record);

    int insertSelective(TClue record);

    TClue selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TClue record);

    int updateByPrimaryKey(TClue record);

    @DataScope(tableAlias = "tc", tableField = "owner_id")
    List<ClueDto> list(BaseQuery query);

	void insertBatch(List<TClue> clueList);

	int countByPhone(String phone);

	ClueDto getClueDetail(Integer id);

	int count();

	List<EchartsData> selectSourceCountByClueSource();
}
