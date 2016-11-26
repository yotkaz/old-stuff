package yotkaz.arduino.gui.sm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import yotkaz.arduino.gui.sm.service.SerialMonitorService;
import yotkaz.arduino.sm.domain.Message;

/**
 * @author yotkaz
 *
 */
@Controller
public class SerialMonitorController {
	
	public static final String SM_PAGE = "sm";
	public static final String SM_PAGE_MAPPING = "/" + SM_PAGE;
	public static final String SM_JSON_MAPPING = SM_PAGE_MAPPING + "/messages";

	@Autowired
	SerialMonitorService service;
	
	@RequestMapping(value = SM_PAGE_MAPPING, method=RequestMethod.POST)
	public String handle(Model model, @RequestParam String text) {
		service.send(text);
		return handle(model);
	}
	
	@RequestMapping(value = SM_PAGE_MAPPING, method=RequestMethod.GET)
	public String handle(Model model) {
		model.addAttribute("messages", service.getAllMessages());
		return SM_PAGE;
	}
	
	@RequestMapping(value = SM_JSON_MAPPING)
	public @ResponseBody List<Message> handleJSON() {
		return service.getAllMessages();
	}
}
