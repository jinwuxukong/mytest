package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbContent;

public interface ContentService {

	DataGridResult getcontentList(Long categoryId,int page,int rows);
	E3Result addContent(TbContent tbContent);
	List<TbContent> getContentAdvertList(long id);
}
