package com.qq.service.impl;

import com.qq.mapper.TProductMapper;
import com.qq.model.po.TProduct;
import com.qq.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	private final TProductMapper tProductMapper;

	@Override
	public List<TProduct> getAllOnSaleProduct() {
		return tProductMapper.selectAllOnSaleProduct();
	}
}
