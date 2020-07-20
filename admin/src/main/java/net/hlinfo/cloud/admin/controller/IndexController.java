package net.hlinfo.cloud.admin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.hlinfo.cloud.entity.Userinfo;
import net.hlinfo.cloud.mybatis.service.MybatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags="首页")
@RestController
@RequestMapping("/")
public class IndexController {

	@Autowired
	private MybatisService mybatisService;

	@ApiOperationSupport(author = "service@hlinfo.net")
	@ApiOperation("欢迎页")
	@GetMapping(value = {"/","/index.html"})
	public String index() {
		
		return "Hello Word admin^_^";
	}

	@ApiOperationSupport(author = "service@hlinfo.net")
	@ApiOperation("测试MyBatis")
	@GetMapping("/ok")
	public String getAreacode() {
		List<Userinfo> list = mybatisService.queryList("find_rootlog_list", Userinfo.class);
		return "ok:"+list;
	}

}
