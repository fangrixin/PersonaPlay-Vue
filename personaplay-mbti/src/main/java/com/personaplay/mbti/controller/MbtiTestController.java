package com.personaplay.mbti.controller;

import com.personaplay.common.core.controller.BaseController;
import com.personaplay.common.core.domain.AjaxResult;
import com.personaplay.mbti.domain.MbtiQuestion;
import com.personaplay.mbti.service.IMbtiQuestionService;
import com.personaplay.mbti.service.IMbtiTestRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * MBTI测试控制器
 *
 * @author fangrx
 */
@RestController
@RequestMapping("/api/mbti/test")
public class MbtiTestController extends BaseController {

    @Autowired
    private IMbtiTestRecordService mbtiTestRecordService;

    @Autowired
    private IMbtiQuestionService mbtiQuestionService;

    /**
     * 处理测试结果
     */
    @PostMapping("/processResult/{roomCode}/{userId}")
    public AjaxResult processResult( @PathVariable String roomCode,
                                     @PathVariable String userId) {
        Map<String, Object> result = mbtiTestRecordService.processTestResult(roomCode, userId);
        return AjaxResult.success(result);
    }

    /**
     * 获取双人测试结果
     */
    @GetMapping("/dualResult")
    public AjaxResult getDualTestResult(@RequestParam String roomCode) {
        Map<String, Object> result = mbtiTestRecordService.getDualTestResult(roomCode);
        return AjaxResult.success(result);
    }

    /**
     * 获取MBTI测试题目列表
     *
     * @param version 测试版本（short:简短版20题，standard:标准版60题，full:完整版93题）
     * @return 测试题目列表
     */
    @GetMapping("/questions/{version}")
    public AjaxResult getTestQuestions(@PathVariable String version) {

        List<MbtiQuestion> questions = mbtiQuestionService.selectSingleMbtiQuestionList(version);
        return AjaxResult.success(questions);
    }

    /**
     * 保存单人测试结果
     */
    @PostMapping("/saveResult")
    public AjaxResult saveTestResult(@RequestBody Map<String, Object> testData) {
        Map<String, Object> result = mbtiTestRecordService.saveTestResult(testData);
        return AjaxResult.success(result);
    }
}
