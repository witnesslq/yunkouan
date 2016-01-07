package com.yunkouan.lpid.biz.screenscompare.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunkouan.lpid.biz.generic.controller.GenericController;
import com.yunkouan.lpid.biz.screenscompare.bo.RunningPackageModel;
import com.yunkouan.lpid.biz.screenscompare.bo.ScreenscompareQueryModel;
import com.yunkouan.lpid.biz.screenscompare.service.ScreensCompareHistoryService;
import com.yunkouan.lpid.biz.screenscompare.service.ScreenscompareService;
import com.yunkouan.lpid.commons.page.Page;
import com.yunkouan.lpid.persistence.entity.RunningPackage;

@Controller
@RequestMapping("/screensCompareHistory")
public class ScreensCompareHistoryController extends GenericController {
	private final static String filePath = "screenscompare";
	@Autowired
	private ScreenscompareService screenscompareService;
	@Autowired
	private ScreensCompareHistoryService screensCompareHistoryService;
	/**
	 * 同屏比对历史列表页面
	 * @param req
	 * @param res
	 * @param queryModel
	 * @param currentPage
	 * @param model
	 * @return 
	 * @since 1.0.0
	 * 2014-12-19-下午2:06:23
	*/
	@RequestMapping(value = "/screensCompareHistory")
	public String checkInfoHistory(ModelMap model) {
		return filePath + "/compareHistory";
	}
	
	/**
	 * 同屏比对历史列表点击查询时调用
	 * 
	 * @param request
	 * @param response
	 * @param queryModel
	 * @param currentPage
	 * @param model
	 * @return 
	 * @since 1.0.0
	 * 2014-12-19-下午2:06:23
	*/
	@RequestMapping(value = "/screensCompareHistoryList")
	public String findCheckResultList(HttpServletRequest request, 
									HttpServletResponse response,
									@ModelAttribute("resultForm") ScreenscompareQueryModel queryModel, 
									Integer currentPage, 
									ModelMap model) {
	    //图片总个数
		int count = screenscompareService.findRunningPackageCount(queryModel);
		
		// 根据总记录数、每页记录数、当前页码生成分页对象
		Page<RunningPackageModel> page = new Page<RunningPackageModel>(count, getPageSize(), currentPage == null? 1 : currentPage);

		List<RunningPackage> baggageList = screenscompareService.findAllRunningPackage(page.getFirstResult(), page.getPageSize(), queryModel);
		
		model.put("baggageList", baggageList);
        //点击查询后，保留住查询的参数
        model.addAttribute("queryModel", queryModel);
        model.addAttribute("page", page);
        return filePath + "/compareHistory";
	}
	
	/**
	 * 通过ID查询行李信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/findRunningPackage")
	@ResponseBody
	public RunningPackageModel findRunningPackage(Integer id){
		return screensCompareHistoryService.getRunningPackageModelById(id);
	}
}
