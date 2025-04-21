package com.qq.service;

import com.qq.common.easyexcel.CustomerExcel;
import com.qq.model.dto.CustomerDto;
import com.qq.model.po.TCustomer;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CustomerService {
	boolean addCustomer(TCustomer customer);

	PageInfo<CustomerDto> getCustomerList(Integer pageNum, Integer pageSize);

	List<CustomerExcel> getCustomerExcelData(Integer[] ids);
}
