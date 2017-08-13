package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.TreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCatService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCatServiceImpl implements ContentCatService {

	@Autowired
	private TbContentCategoryMapper categoryMapper;

	public List<TreeNode> getContentCatList(long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = categoryMapper.selectByExample(example);
		List<TreeNode> list2 = new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(tbContentCategory.getId());
			treeNode.setState(tbContentCategory.getIsParent() ? "closed" : "open");
			treeNode.setText(tbContentCategory.getName());
			list2.add(treeNode);
		}
		return list2;
	}

	public E3Result addContentCategory(long parentId, String name) {
		// 1、创建一个TbContentCategory对象
		TbContentCategory category = new TbContentCategory();
		// 2、补全属性
		category.setParentId(parentId);
		category.setName(name);
		// 1正常，2.删除
		category.setStatus(1);
		category.setSortOrder(1);
		category.setIsParent(false);
		category.setCreated(new Date());
		category.setUpdated(new Date());
		// 3、插入到表中。
		categoryMapper.insert(category);
		// 4、需要判断父节点的isparent
		TbContentCategory parent = categoryMapper.selectByPrimaryKey(parentId);
		// 5、如果父节点的isparent为false应该改为true
		if (!parent.getIsParent()) {
			parent.setIsParent(true);
			categoryMapper.updateByPrimaryKey(parent);
		}
		// 6、返回E3Result其中包含TbContentCategory对象
		return E3Result.ok(category);
	}

	public E3Result updateContentCategory(long parentId, String name) {
		// 根据id查询对象
		TbContentCategory category = categoryMapper.selectByPrimaryKey(parentId);
		// 修改名称
		category.setName(name);
		// 更新对象
		categoryMapper.updateByPrimaryKeySelective(category);
		// 返回e3Result
		return E3Result.ok(category);
	}

	public E3Result deleteContentCategory(long parentId) {
		TbContentCategory contentCategory = categoryMapper.selectByPrimaryKey(parentId);
		//判断是否是文件夹
		if(contentCategory.getIsParent().equals(1)){
			TbContentCategoryExample example = new TbContentCategoryExample();
			Criteria createCriteria = example.createCriteria();
			createCriteria.andParentIdEqualTo(contentCategory.getParentId());
			List<TbContentCategory> list = categoryMapper.selectByExample(example);
			for (TbContentCategory tbContentCategory : list) {
				categoryMapper.deleteByPrimaryKey(tbContentCategory.getId());
			}
		}
		//根据id删除对象
		categoryMapper.deleteByPrimaryKey(parentId);
		
		return E3Result.ok();
	}

}
