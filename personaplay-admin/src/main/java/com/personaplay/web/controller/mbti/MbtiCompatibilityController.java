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
import com.personaplay.mbti.domain.MbtiCompatibility;
import com.personaplay.mbti.service.IMbtiCompatibilityService;
import com.personaplay.common.utils.poi.ExcelUtil;
import com.personaplay.common.core.page.TableDataInfo;

/**
 * MBTI类型兼容性Controller
 *
 * @author fangrx
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/mbti/mbtiCompatibility")
public class MbtiCompatibilityController extends BaseController
{
    @Autowired
    private IMbtiCompatibilityService mbtiCompatibilityService;

    /**
     * 查询MBTI类型兼容性列表
     */
//    @PreAuthorize("@ss.hasPermi('mbti:mbtiCompatibility:list')")
    @GetMapping("/list")
    public TableDataInfo list(MbtiCompatibility mbtiCompatibility)
    {
        startPage();
        List<MbtiCompatibility> list = mbtiCompatibilityService.selectMbtiCompatibilityList(mbtiCompatibility);
        return getDataTable(list);
    }

    /**
     * 导出MBTI类型兼容性列表
     */
//    @PreAuthorize("@ss.hasPermi('mbti:mbtiCompatibility:export')")
    @Log(title = "MBTI类型兼容性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MbtiCompatibility mbtiCompatibility)
    {
        List<MbtiCompatibility> list = mbtiCompatibilityService.selectMbtiCompatibilityList(mbtiCompatibility);
        ExcelUtil<MbtiCompatibility> util = new ExcelUtil<MbtiCompatibility>(MbtiCompatibility.class);
        util.exportExcel(response, list, "MBTI类型兼容性数据");
    }

    /**
     * 获取MBTI类型兼容性详细信息
     */
//    @PreAuthorize("@ss.hasPermi('mbti:mbtiCompatibility:query')")
    @GetMapping(value = "/{compatibilityId}")
    public AjaxResult getInfo(@PathVariable("compatibilityId") Long compatibilityId)
    {
        return success(mbtiCompatibilityService.selectMbtiCompatibilityByCompatibilityId(compatibilityId));
    }

    /**
     * 新增MBTI类型兼容性
     */
//    @PreAuthorize("@ss.hasPermi('mbti:mbtiCompatibility:add')")
    @Log(title = "MBTI类型兼容性", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MbtiCompatibility mbtiCompatibility)
    {
        return toAjax(mbtiCompatibilityService.insertMbtiCompatibility(mbtiCompatibility));
    }

    /**
     * 修改MBTI类型兼容性
     */
//    @PreAuthorize("@ss.hasPermi('mbti:mbtiCompatibility:edit')")
    @Log(title = "MBTI类型兼容性", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MbtiCompatibility mbtiCompatibility)
    {
        return toAjax(mbtiCompatibilityService.updateMbtiCompatibility(mbtiCompatibility));
    }

    /**
     * 删除MBTI类型兼容性
     */
//    @PreAuthorize("@ss.hasPermi('mbti:mbtiCompatibility:remove')")
    @Log(title = "MBTI类型兼容性", businessType = BusinessType.DELETE)
	@DeleteMapping("/{compatibilityIds}")
    public AjaxResult remove(@PathVariable Long[] compatibilityIds)
    {
        return toAjax(mbtiCompatibilityService.deleteMbtiCompatibilityByCompatibilityIds(compatibilityIds));
    }
}
