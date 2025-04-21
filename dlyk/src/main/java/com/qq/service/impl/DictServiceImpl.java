package com.qq.service.impl;

import com.qq.mapper.TDicTypeMapper;
import com.qq.model.dto.DictDto;
import com.qq.service.DictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DictServiceImpl implements DictService {
	private final TDicTypeMapper tDicTypeMapper;

	@Override
	public List<DictDto> loadAllDict() {
		return tDicTypeMapper.selectAll();
	}
}
