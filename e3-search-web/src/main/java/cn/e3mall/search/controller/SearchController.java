package cn.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;

@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;

	@Value("${search.result.rows}")
	private Integer rows;

	@RequestMapping("/search")
	public String search(String keyword,@RequestParam(defaultValue="1")int page,Model model) throws Exception{
		//get转码
		keyword = new String(keyword.getBytes("iso8859-1"),"utf-8");
		//接收页面提示的参数
		//	调用Service查询结果
		SearchResult search = searchService.search(keyword, page, rows);
		//把查询结果传递给jsp
		//查询参数回显
		model.addAttribute("query", keyword);
		model.addAttribute("page", page);
		//查询结果
		model.addAttribute("totalPages", search.getTotalPages());
		model.addAttribute("recourdCount",search.getRecourdCount());
		model.addAttribute("itemList", search.getItemList());
		//返回逻辑视图
		return "search";
	}
}
