package net.hlinfo.cloud.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.hlinfo.cloud.admin.mybatis.service.CoreService;
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
	private CoreService coreService;

	@ApiOperation("欢迎页")
	@GetMapping(value = {"/","/index.html"})
	public String index() {
		
		return "Hello Word admin^_^";
	}

	@ApiOperation("测试MyBatis")
	@GetMapping("/ok")
	public String getAreacode() {
		List list = coreService.queryList("find_rootlog_list");
		return "ok:"+list;
	}

}
