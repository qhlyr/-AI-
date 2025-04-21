package com.qq.service;

import com.qq.model.result.EchartsData;
import com.qq.model.result.SummaryData;

import java.util.List;

public interface DashboardService {
	SummaryData getSummaryData();

	List<EchartsData> getSalesFunnelData();

	List<EchartsData> getClueSourcePieData();
}
