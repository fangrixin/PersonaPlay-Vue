package com.personaplay.mbti.domain.vo;

import com.personaplay.mbti.domain.MbtiTestRecord;

/**
 * MBTI测试记录对象 mbti_test_record
 *
 * @author fangrx
 * @date 2025-03-24
 */

public class MbtiTestRecordVo extends MbtiTestRecord
{


    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    private String versionName;

}
