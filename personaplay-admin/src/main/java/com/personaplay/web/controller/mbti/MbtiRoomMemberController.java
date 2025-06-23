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
import com.personaplay.mbti.domain.MbtiRoomMember;
import com.personaplay.mbti.service.IMbtiRoomMemberService;
import com.personaplay.common.utils.poi.ExcelUtil;
import com.personaplay.common.core.page.TableDataInfo;

/**
 * 房间成员Controller
 *
 * @author fangrx
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/mbti/mbtiRoomMember")
public class MbtiRoomMemberController extends BaseController
{
    @Autowired
    private IMbtiRoomMemberService mbtiRoomMemberService;

    /**
     * 查询房间成员列表
     */
    @PreAuthorize("@ss.hasPermi('mbti:mbtiRoomMember:list')")
    @GetMapping("/list")
    public TableDataInfo list(MbtiRoomMember mbtiRoomMember)
    {
        startPage();
        List<MbtiRoomMember> list = mbtiRoomMemberService.selectMbtiRoomMemberList(mbtiRoomMember);
        return getDataTable(list);
    }

    /**
     * 导出房间成员列表
     */
    @PreAuthorize("@ss.hasPermi('mbti:mbtiRoomMember:export')")
    @Log(title = "房间成员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MbtiRoomMember mbtiRoomMember)
    {
        List<MbtiRoomMember> list = mbtiRoomMemberService.selectMbtiRoomMemberList(mbtiRoomMember);
        ExcelUtil<MbtiRoomMember> util = new ExcelUtil<MbtiRoomMember>(MbtiRoomMember.class);
        util.exportExcel(response, list, "房间成员数据");
    }

    /**
     * 获取房间成员详细信息
     */
    @PreAuthorize("@ss.hasPermi('mbti:mbtiRoomMember:query')")
    @GetMapping(value = "/{memberId}")
    public AjaxResult getInfo(@PathVariable("memberId") Long memberId)
    {
        return success(mbtiRoomMemberService.selectMbtiRoomMemberByMemberId(memberId));
    }

    /**
     * 新增房间成员
     */
    @PreAuthorize("@ss.hasPermi('mbti:mbtiRoomMember:add')")
    @Log(title = "房间成员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MbtiRoomMember mbtiRoomMember)
    {
        return toAjax(mbtiRoomMemberService.insertMbtiRoomMember(mbtiRoomMember));
    }

    /**
     * 修改房间成员
     */
    @PreAuthorize("@ss.hasPermi('mbti:mbtiRoomMember:edit')")
    @Log(title = "房间成员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MbtiRoomMember mbtiRoomMember)
    {
        return toAjax(mbtiRoomMemberService.updateMbtiRoomMember(mbtiRoomMember));
    }

    /**
     * 删除房间成员
     */
    @PreAuthorize("@ss.hasPermi('mbti:mbtiRoomMember:remove')")
    @Log(title = "房间成员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{memberIds}")
    public AjaxResult remove(@PathVariable Long[] memberIds)
    {
        return toAjax(mbtiRoomMemberService.deleteMbtiRoomMemberByMemberIds(memberIds));
    }
}
