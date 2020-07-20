package net.hlinfo.cloud.demo.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.hlinfo.cloud.entity.Userinfo;
import net.hlinfo.cloud.mybatis.service.MybatisService;
import net.hlinfo.opt.Funs;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.lang.util.NutMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags="首页")
@RestController
@RequestMapping("/")
public class IndexController {
	protected Logger log = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private MybatisService coreService;

	@Autowired
	private Dao dao;

	@Value("${spring.love}")
	private String love;

	@Value("${spring.application.name}")
	private String appname;

	@ApiOperationSupport(author = "service@hlinfo.net")
	@ApiOperation("欢迎页")
	@GetMapping(value = {"/","/index.html"})
	public String index() {

		return "Hello Word ^_^"+love;
	}

	@ApiOperationSupport(author = "service@hlinfo.net")
	@ApiOperation("测试MyBatis")
	@GetMapping("/MyBatis")
	public List getAreacode(@ApiParam("页数")@RequestParam(name="page", defaultValue = "1") int page
			, @ApiParam("每页显示条数")@RequestParam(name="limit", defaultValue = "20") int limit) {
		page = page<=0?1:page;
		limit = limit<=0?20:limit;
		int start = (page-1)*limit;
		Map<String,Object> map = new HashMap<>();
		map.put("start",start);
		map.put("limit",limit);
		List<Userinfo> list = coreService.queryList("find_Userinfo_list",Userinfo.class,map);
		//System.out.println(list);
		return list;
	}

	@ApiOperationSupport(author = "service@hlinfo.net")
	@ApiOperation("测试Mutz")
	@GetMapping("/nutz")
	public List<Userinfo> getDataByNutz(@ApiParam("页数")@RequestParam(name="page", defaultValue = "1") int page
			, @ApiParam("每页显示条数")@RequestParam(name="limit", defaultValue = "20") int limit) {
		Pager pager = dao.createPager(page, limit);
		List<Userinfo> list = dao.query(Userinfo.class, Cnd.where("isdelete","=",0),pager);
		//System.out.println(list);
		return list;
	}

	@ApiOperationSupport(author = "service@hlinfo.net")
	@ApiOperation("添加数据")
	@GetMapping("/insert")
	public NutMap insert() {
		for(int i=0;i<10;i++){
			Userinfo u = new Userinfo();
			u.setId(Funs.uuid());
			u.setAccount("test100"+i);
			u.setUsername(Funs.getName());
			u.setPassword("123456");
			u.setSalt(Funs.getRandom(6));
			u.setEmail("12345678@qq.com");
			u.setRemark("备注："+i);
			u.setState(1);
			u.setLoginutime("2020-03-29 14:28");
			u.setLoginuip("10.58.46.10"+i);
			u.setCreatetime(new Date());
			u.setUpdatetime(new Date());
			u.setIsdelete(0);
			dao.insert(u);
		}

		return NutMap.NEW().addv("msg","添加数据成功").addv("code",200);
	}

}
