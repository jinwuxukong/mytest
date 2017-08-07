import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;

public class PageHelperTest {
	
	@Test
	public void testPageHelper(){
		PageHelper.startPage(1, 10);
		//执行查询
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		//从容器中获得mapper代理对象
		TbItemMapper bean = applicationContext.getBean(TbItemMapper.class);
		//执行查询
		TbItemExample tbItemExample = new TbItemExample();
		List<TbItem> list = bean.selectByExample(tbItemExample);
		//去查询结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		System.out.println("总记录数"+pageInfo.getTotal());
		System.out.println("总页数"+pageInfo.getPages());
		System.out.println("list是总记录数"+list.size());
		
	}
}
