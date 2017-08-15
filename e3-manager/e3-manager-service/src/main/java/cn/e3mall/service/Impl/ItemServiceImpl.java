package cn.e3mall.service.Impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;


@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	
	public TbItem getItemById(long itemId) {
		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		return tbItem;
	}

	public DataGridResult getItemList(int page, int rows) {
//		1、设置分页信息
		PageHelper.startPage(page, rows);
//		2、执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
//		3、取查询结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
//			取总记录数
		long total = pageInfo.getTotal();
//			取商品列表
//		4、把结果封装到DataGridResult中
		DataGridResult dataGridResult = new DataGridResult();
		dataGridResult.setTotal(total);
		dataGridResult.setRows(list);
//		5、返回结果
		return dataGridResult;
	}

	public E3Result addItem(TbItem item, String desc) {
		// 1、生成商品id
		long genItemId = IDUtils.genItemId();
		// 2、补全TbItem中的属性
		item.setId(genItemId);
		//1.正常1 2.下架 3.删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 3、插入到商品表。
		itemMapper.insert(item);
		// 4、创建一个商品描述表对应的pojo
		TbItemDesc tbItemDesc = new TbItemDesc();
		// 5、补全属性
		tbItemDesc.setItemId(genItemId);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setUpdated(new Date());
		tbItemDesc.setCreated(new Date());
		// 6、向商品描述表插入数据
		tbItemDescMapper.insert(tbItemDesc);
		// 7、返回E3Result
		
		return E3Result.ok();
	}

}
