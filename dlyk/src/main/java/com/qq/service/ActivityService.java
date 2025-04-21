package com.qq.service;

import com.qq.model.dto.ActivityDto;
import com.qq.model.po.TActivity;
import com.qq.model.po.TActivityRemark;
import com.qq.model.query.ActivityQuery;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ActivityService {
	PageInfo<ActivityDto> list(Integer pageNum, Integer pageSize, ActivityQuery query);

	boolean addActivity(TActivity activity);

	TActivity getActivityInfo(Long id);

	boolean updateActivity(TActivity activity);

	ActivityDto getActivityDetail(Long id);

	boolean addRemark(TActivityRemark remark);

	PageInfo<TActivityRemark> getRemarkList(Long activityId, Integer pageNum, Integer pageSize);

	boolean deleteRemark(Integer id);

	boolean updateRemark(TActivityRemark remark);

	List<TActivity> getOngoingActivityList();
}
