package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;

/**
 * 商品搜索服务
 * @author Administrator
 *
 */
@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;
	
	public SearchResult search(String keyWrod, int page, int rows) throws Exception {
		// 1、创建一个SolrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		// 2、向SolrQuery对象中设置查询条件
		solrQuery.setQuery(keyWrod);
		//分页条件
		solrQuery.setStart((page-1)*rows);
		solrQuery.setRows(rows);
		//设置默搜索域
		solrQuery.set("df","item_title");
		//开启高亮
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</em>");
		// 3、调用dao执行查询
		SearchResult search = searchDao.search(solrQuery);
		// 4、需要计算总页数
		long recourdCount = search.getRecourdCount();
		long PageCount = recourdCount/rows;
		if(recourdCount % rows !=0){
			PageCount++;
		}
		search.setTotalPages(PageCount);
		// 5、返回查询结果
		return search;
	}

}
