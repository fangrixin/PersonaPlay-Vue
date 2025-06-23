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
import com.personaplay.mbti.domain.MbtiUser;
import com.personaplay.mbti.service.IMbtiUserService;
import com.personaplay.common.utils.poi.ExcelUtil;
import com.personaplay.common.core.page.TableDataInfo;

/**
 * MBTI用户信息Controller
 *
 * @author fangrx
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/mbti/mbtiUser")
public class MbtiUserController extends BaseController
{
    @Autowired
    private IMbtiUserService mbtiUserService;

    /**
     * 查询MBTI用户信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MbtiUser mbtiUser)
    {
        startPage();
        List<MbtiUser> list = mbtiUserService.selectMbtiUserList(mbtiUser);
        return getDataTable(list);
    }

    /**
     * 导出MBTI用户信息列表
     */
    @Log(title = "MBTI用户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MbtiUser mbtiUser)
    {
        List<MbtiUser> list = mbtiUserService.selectMbtiUserList(mbtiUser);
        ExcelUtil<MbtiUser> util = new ExcelUtil<MbtiUser>(MbtiUser.class);
        util.exportExcel(response, list, "MBTI用户信息数据");
    }

    /**
     * 获取MBTI用户信息详细信息
     */
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(mbtiUserService.selectMbtiUserByUserId(userId));
    }

    /**
     * 新增MBTI用户信息
     */
    @Log(title = "MBTI用户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MbtiUser mbtiUser)
    {
        return toAjax(mbtiUserService.insertMbtiUser(mbtiUser));
    }

    /**
     * 修改MBTI用户信息
     */
    @Log(title = "MBTI用户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MbtiUser mbtiUser)
    {
        return toAjax(mbtiUserService.updateMbtiUser(mbtiUser));
    }

    /**
     * 删除MBTI用户信息
     */
    @Log(title = "MBTI用户信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(mbtiUserService.deleteMbtiUserByUserIds(userIds));
    }
}
