package com.qq.service;

import com.qq.model.dto.ClueDto;
import com.qq.model.dto.ClueRemarkDto;
import com.qq.model.po.TClue;
import com.qq.model.po.TClueRemark;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

public interface ClueService {
	PageInfo<ClueDto> list(Integer pageNum, Integer pageSize);

	void upload(MultipartFile file);

	boolean checkPhone(String phone);

	boolean addClue(TClue clue);

	ClueDto getClueDetail(Integer id);

	boolean updateClue(TClue clue);

	boolean addClueRemark(TClueRemark remark);

	PageInfo<ClueRemarkDto> getClueRemarks(Integer clueId, Integer pageNum, Integer pageSize);

	boolean deleteClue(Integer id);
}
