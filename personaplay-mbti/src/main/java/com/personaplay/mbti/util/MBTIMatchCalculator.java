
package com.personaplay.mbti.util;

public class MBTIMatchCalculator {

    // MBTI维度得分类
    public static class MBTIScore {
        private final int e; // E得分（0-100）
        private final int s; // S得分（0-100）
        private final int t; // T得分（0-100）
        private final int j; // J得分（0-100）

        public MBTIScore(int e, int s, int t, int j) {
            validateRange(e, s, t, j);
            this.e = e;
            this.s = s;
            this.t = t;
            this.j = j;
        }

        private void validateRange(int... scores) {
            for (int score : scores) {
                if (score < 0 || score > 100) {
                    throw new IllegalArgumentException("得分必须在0-100之间");
                }
            }
        }

        // Getters
        public int getE() { return e; }
        public int getS() { return s; }
        public int getT() { return t; }
        public int getJ() { return j; }
    }

    /**
     * 计算亲密关系匹配度
     * @param scoreA 用户A的MBTI得分
     * @param scoreB 用户B的MBTI得分
     * @return 匹配度百分比（0-100的整数）
     */
    public static int calculateMatch(MBTIScore scoreA, MBTIScore scoreB) {
        // 计算各维度相似度或互补度
        double eiSimilarity = 1 - Math.abs(scoreA.getE() - scoreB.getE()) / 100.0;
        double snComplement = Math.abs(scoreA.getS() - scoreB.getS()) / 100.0;
        double tfComplement = Math.abs(scoreA.getT() - scoreB.getT()) / 100.0;
        double jpSimilarity = 1 - Math.abs(scoreA.getJ() - scoreB.getJ()) / 100.0;

        // 加权计算总匹配度（范围0.0-1.0）
        double match = (eiSimilarity * 0.3) + (snComplement * 0.2) +
                (tfComplement * 0.2) + (jpSimilarity * 0.3);

        // 转换为整数百分比（四舍五入）
        return (int) Math.round(match * 100);
    }

    // 示例
    public static void main(String[] args) {
        // 用户A：E70, S60, T40, J55
        MBTIScore userA = new MBTIScore(70, 60, 40, 55);
        // 用户B：E20（即I80），S70（即N30），T30（即F70），J55（即P45）
        MBTIScore userB = new MBTIScore(20, 70, 30, 55);

        int match = calculateMatch(userA, userB);
        System.out.println("亲密关系匹配度: " + match + "%");
    }
}
