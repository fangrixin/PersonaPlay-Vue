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
import com.personaplay.mbti.domain.MbtiTestVersion;
import com.personaplay.mbti.service.IMbtiTestVersionService;
import com.personaplay.common.utils.poi.ExcelUtil;
import com.personaplay.common.core.page.TableDataInfo;

/**
 * MBTI测试版本Controller
 *
 * @author fangrx
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/mbti/mbtiTestVersion")
public class MbtiTestVersionController extends BaseController
{
    @Autowired
    private IMbtiTestVersionService mbtiTestVersionService;

    /**
     * 查询MBTI测试版本列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MbtiTestVersion mbtiTestVersion)
    {
        startPage();
        List<MbtiTestVersion> list = mbtiTestVersionService.selectMbtiTestVersionList(mbtiTestVersion);
        return getDataTable(list);
    }

    /**
     * 导出MBTI测试版本列表
     */
    @Log(title = "MBTI测试版本", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MbtiTestVersion mbtiTestVersion)
    {
        List<MbtiTestVersion> list = mbtiTestVersionService.selectMbtiTestVersionList(mbtiTestVersion);
        ExcelUtil<MbtiTestVersion> util = new ExcelUtil<MbtiTestVersion>(MbtiTestVersion.class);
        util.exportExcel(response, list, "MBTI测试版本数据");
    }

    /**
     * 获取MBTI测试版本详细信息
     */
    @GetMapping(value = "/{versionId}")
    public AjaxResult getInfo(@PathVariable("versionId") Long versionId)
    {
        return success(mbtiTestVersionService.selectMbtiTestVersionByVersionId(versionId));
    }

    /**
     * 新增MBTI测试版本
     */
    @Log(title = "MBTI测试版本", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MbtiTestVersion mbtiTestVersion)
    {
        return toAjax(mbtiTestVersionService.insertMbtiTestVersion(mbtiTestVersion));
    }

    /**
     * 修改MBTI测试版本
     */
    @Log(title = "MBTI测试版本", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MbtiTestVersion mbtiTestVersion)
    {
        return toAjax(mbtiTestVersionService.updateMbtiTestVersion(mbtiTestVersion));
    }

    /**
     * 删除MBTI测试版本
     */
    @Log(title = "MBTI测试版本", businessType = BusinessType.DELETE)
	@DeleteMapping("/{versionIds}")
    public AjaxResult remove(@PathVariable Long[] versionIds)
    {
        return toAjax(mbtiTestVersionService.deleteMbtiTestVersionByVersionIds(versionIds));
    }
}
