package net.hlinfo.cloud.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class IndexController {

	@Value("${spring.love}")
	private String love;

	@GetMapping(value = {"/","/index.html"})
	public String index() {
		
		return "Hello Word ^_^"+love;
	}
	@GetMapping("/ok")
	public String getAreacode() {

		return "ok";
	}

}
