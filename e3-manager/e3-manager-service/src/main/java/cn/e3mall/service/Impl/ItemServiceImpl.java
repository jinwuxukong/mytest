package cn.e3mall.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;


@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	
	public TbItem getItemById(long itemId) {
		System.out.println("1");
		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		System.out.println(tbItem);
		System.out.println("2");
		return tbItem;
	}

}
