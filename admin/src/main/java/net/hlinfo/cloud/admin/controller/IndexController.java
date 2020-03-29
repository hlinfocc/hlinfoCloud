package net.hlinfo.cloud.admin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class IndexController {

	@Value("${spring.application.name}")
	private String name;

	@GetMapping(value = {"/","/index.html"})
	public String index() {
		
		return "Hello Word ^_^"+name;
	}
	@GetMapping("/ok")
	public String getAreacode() {

		return "ok";
	}

}