package huy.ho.myfirstweb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
	@GetMapping("/")
	public String index() {
        // Logic website
		return "index";
	}
}