package com.personaplay.web.controller.mbti;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.personaplay.common.core.domain.model.LoginUser;
import com.personaplay.framework.web.service.TokenService;
import com.personaplay.mbti.domain.vo.MbtiTestRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.personaplay.common.annotation.Log;
import com.personaplay.common.core.controller.BaseController;
import com.personaplay.common.core.domain.AjaxResult;
import com.personaplay.common.enums.BusinessType;
import com.personaplay.mbti.domain.MbtiTestRecord;
import com.personaplay.mbti.service.IMbtiTestRecordService;
import com.personaplay.common.utils.poi.ExcelUtil;
import com.personaplay.common.core.page.TableDataInfo;

/**
 * MBTI测试记录Controller
 *
 * @author fangrx
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/mbti/mbtiTestRecord")
public class MbtiTestRecordController extends BaseController
{
    @Autowired
    private IMbtiTestRecordService mbtiTestRecordService;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询MBTI测试记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MbtiTestRecord mbtiTestRecord)
    {
        startPage();
        List<MbtiTestRecord> list = mbtiTestRecordService.selectMbtiTestRecordList(mbtiTestRecord);
        return getDataTable(list);
    }

    /**
     * 导出MBTI测试记录列表
     */
    @Log(title = "MBTI测试记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MbtiTestRecord mbtiTestRecord)
    {
        List<MbtiTestRecord> list = mbtiTestRecordService.selectMbtiTestRecordList(mbtiTestRecord);
        ExcelUtil<MbtiTestRecord> util = new ExcelUtil<MbtiTestRecord>(MbtiTestRecord.class);
        util.exportExcel(response, list, "MBTI测试记录数据");
    }

    /**
     * 获取MBTI测试记录详细信息
     */
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        return success(mbtiTestRecordService.selectMbtiTestRecordByRecordId(recordId));
    }

    /**
     * 新增MBTI测试记录
     */
    @Log(title = "MBTI测试记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MbtiTestRecord mbtiTestRecord)
    {
        return toAjax(mbtiTestRecordService.insertMbtiTestRecord(mbtiTestRecord));
    }

    /**
     * 修改MBTI测试记录
     */
    @Log(title = "MBTI测试记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MbtiTestRecord mbtiTestRecord)
    {
        return toAjax(mbtiTestRecordService.updateMbtiTestRecord(mbtiTestRecord));
    }

    /**
     * 删除MBTI测试记录
     */
    @Log(title = "MBTI测试记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(mbtiTestRecordService.deleteMbtiTestRecordByRecordIds(recordIds));
    }

    /**
     * 查询MBTI测试记录列表
     */
    @GetMapping("/listVo")
    public TableDataInfo listVo(MbtiTestRecord mbtiTestRecord,HttpServletRequest request)
    {
        startPage();
        mbtiTestRecord.setUserId(getLoginUser().getUserId());
        if(mbtiTestRecord.getUserId() == null){
            return getDataTable(null);
        }
        List<MbtiTestRecordVo> list = mbtiTestRecordService.selectMbtiTestRecordVoList(mbtiTestRecord);
        return getDataTable(list);
    }

    @GetMapping("/getCompatibilityRanks")
    public AjaxResult getCompatibilityRanks(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        return AjaxResult.success(mbtiTestRecordService.getCompatibilityRanks(loginUser.getUserId()));
    }

    @GetMapping("/getChemistryRanks")
    public AjaxResult getChemistryRanks(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        return AjaxResult.success(mbtiTestRecordService.getChemistryRanks(loginUser.getUserId()));
    }
}
