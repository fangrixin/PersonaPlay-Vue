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
import com.personaplay.mbti.domain.MbtiUserProfile;
import com.personaplay.mbti.service.IMbtiUserProfileService;
import com.personaplay.common.utils.poi.ExcelUtil;
import com.personaplay.common.core.page.TableDataInfo;

/**
 * 用户MBTI档案Controller
 *
 * @author fangrx
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/mbti/mbtiUserProfile")
public class MbtiUserProfileController extends BaseController
{
    @Autowired
    private IMbtiUserProfileService mbtiUserProfileService;

    /**
     * 查询用户MBTI档案列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MbtiUserProfile mbtiUserProfile)
    {
        startPage();
        List<MbtiUserProfile> list = mbtiUserProfileService.selectMbtiUserProfileList(mbtiUserProfile);
        return getDataTable(list);
    }

    /**
     * 导出用户MBTI档案列表
     */
    @Log(title = "用户MBTI档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MbtiUserProfile mbtiUserProfile)
    {
        List<MbtiUserProfile> list = mbtiUserProfileService.selectMbtiUserProfileList(mbtiUserProfile);
        ExcelUtil<MbtiUserProfile> util = new ExcelUtil<MbtiUserProfile>(MbtiUserProfile.class);
        util.exportExcel(response, list, "用户MBTI档案数据");
    }

    /**
     * 获取用户MBTI档案详细信息
     */
    @GetMapping(value = "/{profileId}")
    public AjaxResult getInfo(@PathVariable("profileId") Long profileId)
    {
        return success(mbtiUserProfileService.selectMbtiUserProfileByProfileId(profileId));
    }

    /**
     * 新增用户MBTI档案
     */
    @Log(title = "用户MBTI档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MbtiUserProfile mbtiUserProfile)
    {
        return toAjax(mbtiUserProfileService.insertMbtiUserProfile(mbtiUserProfile));
    }

    /**
     * 修改用户MBTI档案
     */
    @Log(title = "用户MBTI档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MbtiUserProfile mbtiUserProfile)
    {
        return toAjax(mbtiUserProfileService.updateMbtiUserProfile(mbtiUserProfile));
    }

    /**
     * 删除用户MBTI档案
     */
    @Log(title = "用户MBTI档案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{profileIds}")
    public AjaxResult remove(@PathVariable Long[] profileIds)
    {
        return toAjax(mbtiUserProfileService.deleteMbtiUserProfileByProfileIds(profileIds));
    }
}
