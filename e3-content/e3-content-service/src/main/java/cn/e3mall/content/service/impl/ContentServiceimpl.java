package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;
@Service
public class ContentServiceimpl implements ContentService {

	@Autowired
	private JedisClient jedisClient;
	
	@Autowired
	private TbContentMapper contentMapper;
	
	public DataGridResult getcontentList(Long categoryId, int page, int rows) {
		// 1、设置分页信息。使用Mybatis的分页插件
		PageHelper.startPage(page, rows);
		// 2、执行查询，根据分类id查询
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		// 3、取分页结果total
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		// 4、封装到DataGridResult对象中
		DataGridResult dataGridResult = new DataGridResult();
		dataGridResult.setRows(list);
		dataGridResult.setTotal(total);
		// 5、返回DataGridResult
		return dataGridResult;
	}

	public E3Result addContent(TbContent tbContent) {
		// 1、补全pojo对象的属性
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		// 2、插入到数据库中
		contentMapper.insert(tbContent);
		try {
			String hget = jedisClient.hget("content-info", tbContent.getCategoryId().toString());
			if( hget!=null && hget!=""){
				//清空缓存
				jedisClient.hdel("content-info", tbContent.getCategoryId().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 3、返回成功
		return E3Result.ok();
	}

	public List<TbContent> getContentAdvertList(long id) {
		//查询数据库之前查询缓存
		try {
			String json = jedisClient.hget("content-info", id+"");
			//如果查询到结果直接返回
			if(StringUtils.isNoneBlank(json)){
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//如果查询到结果直接返回
		//如果没有结果查询数据库
		//1.创建一个查询条件，根据内容分类id查询
		TbContentExample example = new TbContentExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andCategoryIdEqualTo(id);
		//执行查询
		List<TbContent> list = contentMapper.selectByExample(example);
		//把结果添加到缓存
		try {
			jedisClient.hset("content-info", id+"", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		//返回结果
		return list;
	}

}
