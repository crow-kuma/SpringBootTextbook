package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello3Controller {
	@GetMapping("/hello3/{name}/{age}")
	public String sayHello(@PathVariable("name") String name,@PathVariable("age") int age) {
		return "Hello, world!" + "こんにちは" + age + "歳の" + name + "さん!";
	}
}
