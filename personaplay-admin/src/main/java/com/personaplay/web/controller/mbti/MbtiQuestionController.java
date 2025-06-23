package com.personaplay.web.controller.mbti;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.personaplay.common.annotation.Log;
import com.personaplay.common.core.controller.BaseController;
import com.personaplay.common.core.domain.AjaxResult;
import com.personaplay.common.enums.BusinessType;
import com.personaplay.mbti.domain.MbtiQuestion;
import com.personaplay.mbti.service.IMbtiQuestionService;
import com.personaplay.common.utils.poi.ExcelUtil;
import com.personaplay.common.core.page.TableDataInfo;

/**
 * MBTI测试问题Controller
 *
 * @author fangrx
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/mbti/mbtiQuestion")
public class MbtiQuestionController extends BaseController
{
    @Autowired
    private IMbtiQuestionService mbtiQuestionService;

    /**
     * 查询MBTI测试问题列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MbtiQuestion mbtiQuestion)
    {
        startPage();
        List<MbtiQuestion> list = mbtiQuestionService.selectMbtiQuestionList(mbtiQuestion);
        return getDataTable(list);
    }

    /**
     * 导出MBTI测试问题列表
     */
    @Log(title = "MBTI测试问题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MbtiQuestion mbtiQuestion)
    {
        List<MbtiQuestion> list = mbtiQuestionService.selectMbtiQuestionList(mbtiQuestion);
        ExcelUtil<MbtiQuestion> util = new ExcelUtil<MbtiQuestion>(MbtiQuestion.class);
        util.exportExcel(response, list, "MBTI测试问题数据");
    }

    /**
     * 获取MBTI测试问题详细信息
     */
    @GetMapping(value = "/{questionId}")
    public AjaxResult getInfo(@PathVariable("questionId") Long questionId)
    {
        return success(mbtiQuestionService.selectMbtiQuestionByQuestionId(questionId));
    }

    /**
     * 新增MBTI测试问题
     */
    @Log(title = "MBTI测试问题", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MbtiQuestion mbtiQuestion)
    {
        return toAjax(mbtiQuestionService.insertMbtiQuestion(mbtiQuestion));
    }

    /**
     * 修改MBTI测试问题
     */
    @Log(title = "MBTI测试问题", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MbtiQuestion mbtiQuestion)
    {
        return toAjax(mbtiQuestionService.updateMbtiQuestion(mbtiQuestion));
    }

    /**
     * 删除MBTI测试问题
     */
    @Log(title = "MBTI测试问题", businessType = BusinessType.DELETE)
	@DeleteMapping("/{questionIds}")
    public AjaxResult remove(@PathVariable Long[] questionIds)
    {
        return toAjax(mbtiQuestionService.deleteMbtiQuestionByQuestionIds(questionIds));
    }
}
