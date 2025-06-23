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
import com.personaplay.mbti.domain.MbtiPersonalityType;
import com.personaplay.mbti.service.IMbtiPersonalityTypeService;
import com.personaplay.common.utils.poi.ExcelUtil;
import com.personaplay.common.core.page.TableDataInfo;

/**
 * MBTI人格类型Controller
 *
 * @author fangrx
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/mbti/mbtiPersonalityType")
public class MbtiPersonalityTypeController extends BaseController
{
    @Autowired
    private IMbtiPersonalityTypeService mbtiPersonalityTypeService;

    /**
     * 查询MBTI人格类型列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MbtiPersonalityType mbtiPersonalityType)
    {
        startPage();
        List<MbtiPersonalityType> list = mbtiPersonalityTypeService.selectMbtiPersonalityTypeList(mbtiPersonalityType);
        return getDataTable(list);
    }

    /**
     * 导出MBTI人格类型列表
     */

    @Log(title = "MBTI人格类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MbtiPersonalityType mbtiPersonalityType)
    {
        List<MbtiPersonalityType> list = mbtiPersonalityTypeService.selectMbtiPersonalityTypeList(mbtiPersonalityType);
        ExcelUtil<MbtiPersonalityType> util = new ExcelUtil<MbtiPersonalityType>(MbtiPersonalityType.class);
        util.exportExcel(response, list, "MBTI人格类型数据");
    }

    /**
     * 获取MBTI人格类型详细信息
     */

    @GetMapping(value = "/{typeId}")
    public AjaxResult getInfo(@PathVariable("typeId") Long typeId)
    {
        return success(mbtiPersonalityTypeService.selectMbtiPersonalityTypeByTypeId(typeId));
    }

    /**
     * 新增MBTI人格类型
     */
    @Log(title = "MBTI人格类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MbtiPersonalityType mbtiPersonalityType)
    {
        return toAjax(mbtiPersonalityTypeService.insertMbtiPersonalityType(mbtiPersonalityType));
    }

    /**
     * 修改MBTI人格类型
     */
    @Log(title = "MBTI人格类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MbtiPersonalityType mbtiPersonalityType)
    {
        return toAjax(mbtiPersonalityTypeService.updateMbtiPersonalityType(mbtiPersonalityType));
    }

    /**
     * 删除MBTI人格类型
     */
    @Log(title = "MBTI人格类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{typeIds}")
    public AjaxResult remove(@PathVariable Long[] typeIds)
    {
        return toAjax(mbtiPersonalityTypeService.deleteMbtiPersonalityTypeByTypeIds(typeIds));
    }
}
