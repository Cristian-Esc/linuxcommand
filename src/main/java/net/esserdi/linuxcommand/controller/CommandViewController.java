package net.esserdi.linuxcommand.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/commands")
public class CommandViewController {
	@GetMapping
	public String showCommands(Model model) {
		return "commands";
	}
}
