package com.example.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.createForm.CreateSearchForm;
import com.example.form.SearchPlanForm;
import com.example.service.CreateFormService;
import com.example.service.SearchPlanService;

@Controller
@RequestMapping("/plan")
public class PlanListController {

	@Autowired
	private CreateFormService createFormService;
	
	@Autowired
	private SearchPlanService searchPlanService;
	
	/**
	 * プラン一覧を表示する
	 * @return　プラン一覧画面
	 */
	@RequestMapping("/")
	public String index() {
		return "plan_list";
	}
	
	/**
	 *検索条件入力欄を生成する 
	 * @return　検索条件入力欄
	 */
	@GetMapping("/form")
	@ResponseBody
	public Map<String,CreateSearchForm> showSearchPage() {
		Map <String, CreateSearchForm> form = new HashMap<>();
		form.put("form", createFormService.findSearchForm());
		 return form;
	}

	/**
	 * プランを検索する
	 * @param form
	 * @return
	 */
	@PostMapping("/search")
	@ResponseBody
	public Map<String,Object> searchPlan(@RequestBody SearchPlanForm form){
		Map <String, Object> map = new HashMap<>();
		map.put("planList", searchPlanService.searchPlan(form));
		return map;
	}
	
}
