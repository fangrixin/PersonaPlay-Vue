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
import com.personaplay.mbti.domain.MbtiUserRelationship;
import com.personaplay.mbti.service.IMbtiUserRelationshipService;
import com.personaplay.common.utils.poi.ExcelUtil;
import com.personaplay.common.core.page.TableDataInfo;

/**
 * 用户关系Controller
 *
 * @author fangrx
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/mbti/mbtiUserRelationship")
public class MbtiUserRelationshipController extends BaseController
{
    @Autowired
    private IMbtiUserRelationshipService mbtiUserRelationshipService;

    /**
     * 查询用户关系列表
     */
    @PreAuthorize("@ss.hasPermi('mbti:mbtiUserRelationship:list')")
    @GetMapping("/list")
    public TableDataInfo list(MbtiUserRelationship mbtiUserRelationship)
    {
        startPage();
        List<MbtiUserRelationship> list = mbtiUserRelationshipService.selectMbtiUserRelationshipList(mbtiUserRelationship);
        return getDataTable(list);
    }

    /**
     * 导出用户关系列表
     */
    @PreAuthorize("@ss.hasPermi('mbti:mbtiUserRelationship:export')")
    @Log(title = "用户关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MbtiUserRelationship mbtiUserRelationship)
    {
        List<MbtiUserRelationship> list = mbtiUserRelationshipService.selectMbtiUserRelationshipList(mbtiUserRelationship);
        ExcelUtil<MbtiUserRelationship> util = new ExcelUtil<MbtiUserRelationship>(MbtiUserRelationship.class);
        util.exportExcel(response, list, "用户关系数据");
    }

    /**
     * 获取用户关系详细信息
     */
    @PreAuthorize("@ss.hasPermi('mbti:mbtiUserRelationship:query')")
    @GetMapping(value = "/{relationshipId}")
    public AjaxResult getInfo(@PathVariable("relationshipId") Long relationshipId)
    {
        return success(mbtiUserRelationshipService.selectMbtiUserRelationshipByRelationshipId(relationshipId));
    }

    /**
     * 新增用户关系
     */
    @PreAuthorize("@ss.hasPermi('mbti:mbtiUserRelationship:add')")
    @Log(title = "用户关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MbtiUserRelationship mbtiUserRelationship)
    {
        return toAjax(mbtiUserRelationshipService.insertMbtiUserRelationship(mbtiUserRelationship));
    }

    /**
     * 修改用户关系
     */
    @PreAuthorize("@ss.hasPermi('mbti:mbtiUserRelationship:edit')")
    @Log(title = "用户关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MbtiUserRelationship mbtiUserRelationship)
    {
        return toAjax(mbtiUserRelationshipService.updateMbtiUserRelationship(mbtiUserRelationship));
    }

    /**
     * 删除用户关系
     */
    @PreAuthorize("@ss.hasPermi('mbti:mbtiUserRelationship:remove')")
    @Log(title = "用户关系", businessType = BusinessType.DELETE)
	@DeleteMapping("/{relationshipIds}")
    public AjaxResult remove(@PathVariable Long[] relationshipIds)
    {
        return toAjax(mbtiUserRelationshipService.deleteMbtiUserRelationshipByRelationshipIds(relationshipIds));
    }
}
