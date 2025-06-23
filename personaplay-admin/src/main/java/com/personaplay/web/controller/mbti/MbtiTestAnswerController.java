package com.personaplay.web.controller.mbti;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.personaplay.mbti.domain.MbtiTestAnswer;
import com.personaplay.mbti.service.IMbtiTestAnswerService;
import com.personaplay.common.utils.poi.ExcelUtil;
import com.personaplay.common.core.page.TableDataInfo;

/**
 * 测试答题记录Controller
 *
 * @author fangrx
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/mbti/mbtiTestAnswer")
public class MbtiTestAnswerController extends BaseController
{
    @Autowired
    private IMbtiTestAnswerService mbtiTestAnswerService;

    /**
     * 查询测试答题记录列表
     */
    @PreAuthorize("@ss.hasPermi('mbti:mbtiTestAnswer:list')")
    @GetMapping("/list")
    public TableDataInfo list(MbtiTestAnswer mbtiTestAnswer)
    {
        startPage();
        List<MbtiTestAnswer> list = mbtiTestAnswerService.selectMbtiTestAnswerList(mbtiTestAnswer);
        return getDataTable(list);
    }

    /**
     * 导出测试答题记录列表
     */
    @PreAuthorize("@ss.hasPermi('mbti:mbtiTestAnswer:export')")
    @Log(title = "测试答题记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MbtiTestAnswer mbtiTestAnswer)
    {
        List<MbtiTestAnswer> list = mbtiTestAnswerService.selectMbtiTestAnswerList(mbtiTestAnswer);
        ExcelUtil<MbtiTestAnswer> util = new ExcelUtil<MbtiTestAnswer>(MbtiTestAnswer.class);
        util.exportExcel(response, list, "测试答题记录数据");
    }

    /**
     * 获取测试答题记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('mbti:mbtiTestAnswer:query')")
    @GetMapping(value = "/{answerId}")
    public AjaxResult getInfo(@PathVariable("answerId") Long answerId)
    {
        return success(mbtiTestAnswerService.selectMbtiTestAnswerByAnswerId(answerId));
    }

    /**
     * 新增测试答题记录
     */
    @PreAuthorize("@ss.hasPermi('mbti:mbtiTestAnswer:add')")
    @Log(title = "测试答题记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MbtiTestAnswer mbtiTestAnswer)
    {
        return toAjax(mbtiTestAnswerService.insertMbtiTestAnswer(mbtiTestAnswer));
    }

    /**
     * 修改测试答题记录
     */
    @PreAuthorize("@ss.hasPermi('mbti:mbtiTestAnswer:edit')")
    @Log(title = "测试答题记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MbtiTestAnswer mbtiTestAnswer)
    {
        return toAjax(mbtiTestAnswerService.updateMbtiTestAnswer(mbtiTestAnswer));
    }

    /**
     * 删除测试答题记录
     */
    @PreAuthorize("@ss.hasPermi('mbti:mbtiTestAnswer:remove')")
    @Log(title = "测试答题记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{answerIds}")
    public AjaxResult remove(@PathVariable Long[] answerIds)
    {
        return toAjax(mbtiTestAnswerService.deleteMbtiTestAnswerByAnswerIds(answerIds));
    }
}
