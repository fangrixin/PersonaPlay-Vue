package com.personaplay.web.controller.mbti;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.personaplay.common.core.domain.model.LoginUser;
import com.personaplay.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.personaplay.common.annotation.Log;
import com.personaplay.common.core.controller.BaseController;
import com.personaplay.common.core.domain.AjaxResult;
import com.personaplay.common.enums.BusinessType;
import com.personaplay.mbti.domain.MbtiRoom;
import com.personaplay.mbti.service.IMbtiRoomService;
import com.personaplay.common.utils.poi.ExcelUtil;
import com.personaplay.common.core.page.TableDataInfo;

/**
 * MBTI测试房间Controller
 *
 * @author fangrx
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/mbti/mbtiRoom")
public class MbtiRoomController extends BaseController
{
    @Autowired
    private IMbtiRoomService mbtiRoomService;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询MBTI测试房间列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MbtiRoom mbtiRoom)
    {
        startPage();
        List<MbtiRoom> list = mbtiRoomService.selectMbtiRoomList(mbtiRoom);
        return getDataTable(list);
    }

    /**
     * 导出MBTI测试房间列表
     */
    @Log(title = "MBTI测试房间", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MbtiRoom mbtiRoom)
    {
        List<MbtiRoom> list = mbtiRoomService.selectMbtiRoomList(mbtiRoom);
        ExcelUtil<MbtiRoom> util = new ExcelUtil<MbtiRoom>(MbtiRoom.class);
        util.exportExcel(response, list, "MBTI测试房间数据");
    }

    /**
     * 获取MBTI测试房间详细信息
     */
    @GetMapping(value = "/{roomId}")
    public AjaxResult getInfo(@PathVariable("roomId") Long roomId)
    {
        return success(mbtiRoomService.selectMbtiRoomByRoomId(roomId));
    }

    /**
     * 新增MBTI测试房间
     */
    @Log(title = "MBTI测试房间", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MbtiRoom mbtiRoom)
    {
        return toAjax(mbtiRoomService.insertMbtiRoom(mbtiRoom));
    }

    /**
     * 修改MBTI测试房间
     */
    @Log(title = "MBTI测试房间", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MbtiRoom mbtiRoom)
    {
        return toAjax(mbtiRoomService.updateMbtiRoom(mbtiRoom));
    }

    /**
     * 删除MBTI测试房间
     */
    @Log(title = "MBTI测试房间", businessType = BusinessType.DELETE)
	@DeleteMapping("/{roomIds}")
    public AjaxResult remove(@PathVariable Long[] roomIds)
    {
        return toAjax(mbtiRoomService.deleteMbtiRoomByRoomIds(roomIds));
    }

    /**
     * 创建房间
     */
    @PostMapping("/create")
    public AjaxResult createRoom(@RequestBody Map<String, Object> roomData, HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        roomData.put("creatorId", loginUser.getUserId());
        String roomCode = mbtiRoomService.createRoom(roomData);
        return AjaxResult.success("创建成功", roomCode);
    }

    /**
     * 获取房间信息
     */
    @GetMapping("/info/{roomCode}")
    public AjaxResult getRoomInfo(@PathVariable String roomCode) {
        Map<String, Object> roomInfo = mbtiRoomService.getRoomInfo(roomCode);
        if (roomInfo == null) {
            return AjaxResult.error("房间不存在");
        }
        return AjaxResult.success(roomInfo);
    }

    /**
     * 加入房间
     */
    @PostMapping("/join/{roomCode}")
    public AjaxResult joinRoom(@PathVariable String roomCode, @RequestBody Map<String, Object> userData, HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        userData.put("userId", loginUser.getUserId());
        boolean success = mbtiRoomService.joinRoom(roomCode, userData);
        if (!success) {
            return AjaxResult.error("加入房间失败");
        }
        return AjaxResult.success("加入成功");
    }

    /**
     * 获取房间成员
     */
    @GetMapping("/members/{roomCode}")
    public AjaxResult getRoomMembers(@PathVariable String roomCode) {
        List<Map<String, Object>> members = mbtiRoomService.getRoomMembers(roomCode);
        return AjaxResult.success(members);
    }

    /**
     * 更新准备状态
     */
    @PostMapping("/ready/{roomCode}/{userId}")
    public AjaxResult updateReadyStatus(
            @PathVariable String roomCode,
            @PathVariable String userId,
            @RequestBody Map param,HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        boolean success = mbtiRoomService.updateReadyStatus(roomCode, String.valueOf(loginUser.getUserId()), (Boolean) param.get("isReady"));
        if (!success) {
            return AjaxResult.error("更新准备状态失败");
        }
        return AjaxResult.success("更新准备状态成功");
    }

    /**
     * 开始测试
     */
    @PostMapping("/start/{roomCode}/{userId}")
    public AjaxResult startTest(
            @PathVariable String roomCode,
            @PathVariable String userId,HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        boolean success = mbtiRoomService.startTest(roomCode, String.valueOf(loginUser.getUserId()));
        if (!success) {
            return AjaxResult.error("开始测试失败");
        }
        return AjaxResult.success("开始测试成功");
    }

    /**
     * 更新答题进度
     */
    @PostMapping("/answer/{roomCode}/{userId}")
    public AjaxResult updateAnswer(
            @PathVariable String roomCode,
            @PathVariable String userId,
            @RequestParam int questionIndex,
            @RequestParam String answer) {
        boolean success = mbtiRoomService.updateAnswer(roomCode, userId, questionIndex, answer);
        if (!success) {
            return AjaxResult.error("更新答题进度失败");
        }
        return AjaxResult.success("更新答题进度成功");
    }

    /**
     * 关闭房间
     */
    @PostMapping("/close/{roomCode}/{userId}")
    public AjaxResult closeRoom(
            @PathVariable String roomCode,
            @PathVariable String userId,HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        boolean success = mbtiRoomService.closeRoom(roomCode, String.valueOf(loginUser.getUserId()));
        if (!success) {
            return AjaxResult.error("关闭房间失败");
        }
        return AjaxResult.success("关闭房间成功");
    }
}
