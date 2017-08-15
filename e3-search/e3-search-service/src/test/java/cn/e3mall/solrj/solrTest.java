package cn.e3mall.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class solrTest {

	@Test
	public void addDocument() throws Exception{
		//创建一个slorServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.169.128:8085/solr/collection1");
		//创建一个文档对象
		SolrInputDocument document = new SolrInputDocument();
		//向文档对象中添加域
		document.addField("id", "1");
		document.addField("item_title", "测试商品标价");
		document.addField("item_price", 1000);
		//把文档对象写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	
	@Test
	public void deleteDocumentById() throws Exception{
		//创建一个slorServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.169.128:8085/solr/collection1");
		//根据id删除
		solrServer.deleteById("1");
		//提交
		solrServer.commit();
	}
	
	@Test
	public void deleteDocumentByQuery() throws Exception{
		//创建一个slorServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.169.128:8085/solr/collection1");
		//根据查询条件删除
		solrServer.deleteByQuery("*:*");
		//提交
		solrServer.commit();
	}
	
	@Test
	public void searchIndex() throws Exception{
		//创建一个SolrServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.169.128:8085/solr/collection1");
		//创建一个SolrQuery对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery("*:*");
//				query.set("q", "*:*");
		//执行查询得到一个QueryResponse对象。
		QueryResponse queryResponse = solrServer.query(query);
		//取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		//查询结果的总记录数
		System.out.println(solrDocumentList.getNumFound());
		//查询结果列表
		//打印结果
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_sell_point"));
		}
	}
	
	@Test
	public void searchIndexWithHighLight() throws Exception {
		//创建一个SolrServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.169.128:8085/solr/collection1");
		//创建一个SolrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		//设置查询条件
		solrQuery.setQuery("手机");
		solrQuery.set("df", "item_title");
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em>");
		solrQuery.setHighlightSimplePost("</em>");
		//执行查询
		QueryResponse queryResponse = solrServer.query(solrQuery);
		//取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		//取查询结果总记录数
		System.out.println(solrDocumentList.getNumFound());
		//取高亮显示
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		//遍历列表，取高亮结果
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			//取高亮结果
			String title = "";
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			if (list != null && list.size() > 0) {
				title = list.get(0);
			} else {
				title = solrDocument.get("item_title").toString();
			}
			System.out.println(title);
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_sell_point"));
		}
	}
	
}
