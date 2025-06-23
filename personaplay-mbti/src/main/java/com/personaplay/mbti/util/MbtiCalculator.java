package com.personaplay.mbti.util;

import java.util.HashMap;
import java.util.Map;

/**
 * MBTI计算工具类
 *
 * @author fangrx
 */
public class MbtiCalculator {

    /**
     * 计算MBTI各维度得分
     *
     * @param answers 答题数据，key为题目索引，value为选择的答案
     * @return 各维度得分
     */
    public static Map<String, Integer> calculateScores(Map<String, String> answers) {
        Map<String, Integer> scores = new HashMap<>();

        // 初始化各维度分数
        int eiScore = 0;
        int snScore = 0;
        int tfScore = 0;
        int jpScore = 0;

        // 遍历所有答案
        for (Map.Entry<String, String> entry : answers.entrySet()) {
            int questionIndex = Integer.parseInt(entry.getKey());
            String answer = entry.getValue();

            // 根据题目索引判断是哪个维度的题目
            // 这里示例中使用模4的余数来确定维度，实际应用中可能需要更复杂的映射
            int dimension = questionIndex % 4;

            // 根据答案计算得分
            int score = "A".equalsIgnoreCase(answer) ? 1 : -1;

            // 累加到对应维度
            switch (dimension) {
                case 0:
                    eiScore += score;
                    break;
                case 1:
                    snScore += score;
                    break;
                case 2:
                    tfScore += score;
                    break;
                case 3:
                    jpScore += score;
                    break;
            }
        }

        // 存储各维度得分
        scores.put("EI", eiScore);
        scores.put("SN", snScore);
        scores.put("TF", tfScore);
        scores.put("JP", jpScore);

        return scores;
    }

    /**
     * 根据各维度得分确定MBTI类型
     *
     * @param scores 各维度得分
     * @return MBTI类型
     */
    public static String determineMbtiType(Map<String, Integer> scores) {
        StringBuilder mbtiType = new StringBuilder();

        // E/I维度
        mbtiType.append(scores.get("EI") >= 0 ? "E" : "I");

        // S/N维度
        mbtiType.append(scores.get("SN") >= 0 ? "S" : "N");

        // T/F维度
        mbtiType.append(scores.get("TF") >= 0 ? "T" : "F");

        // J/P维度
        mbtiType.append(scores.get("JP") >= 0 ? "J" : "P");

        return mbtiType.toString();
    }
}
