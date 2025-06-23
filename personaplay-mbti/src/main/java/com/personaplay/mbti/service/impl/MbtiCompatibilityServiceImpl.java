package com.personaplay.mbti.service.impl;

import java.util.List;
import com.personaplay.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.personaplay.mbti.mapper.MbtiCompatibilityMapper;
import com.personaplay.mbti.domain.MbtiCompatibility;
import com.personaplay.mbti.service.IMbtiCompatibilityService;

/**
 * MBTI类型兼容性Service业务层处理
 *
 * @author fangrx
 * @date 2025-03-24
 */
@Service
public class MbtiCompatibilityServiceImpl implements IMbtiCompatibilityService
{
    @Autowired
    private MbtiCompatibilityMapper mbtiCompatibilityMapper;

    /**
     * 查询MBTI类型兼容性
     *
     * @param compatibilityId MBTI类型兼容性主键
     * @return MBTI类型兼容性
     */
    @Override
    public MbtiCompatibility selectMbtiCompatibilityByCompatibilityId(Long compatibilityId)
    {
        return mbtiCompatibilityMapper.selectMbtiCompatibilityByCompatibilityId(compatibilityId);
    }

    /**
     * 查询MBTI类型兼容性列表
     *
     * @param mbtiCompatibility MBTI类型兼容性
     * @return MBTI类型兼容性
     */
    @Override
    public List<MbtiCompatibility> selectMbtiCompatibilityList(MbtiCompatibility mbtiCompatibility)
    {
        return mbtiCompatibilityMapper.selectMbtiCompatibilityList(mbtiCompatibility);
    }

    /**
     * 新增MBTI类型兼容性
     *
     * @param mbtiCompatibility MBTI类型兼容性
     * @return 结果
     */
    @Override
    public int insertMbtiCompatibility(MbtiCompatibility mbtiCompatibility)
    {
        mbtiCompatibility.setCreateTime(DateUtils.getNowDate());
        return mbtiCompatibilityMapper.insertMbtiCompatibility(mbtiCompatibility);
    }

    /**
     * 修改MBTI类型兼容性
     *
     * @param mbtiCompatibility MBTI类型兼容性
     * @return 结果
     */
    @Override
    public int updateMbtiCompatibility(MbtiCompatibility mbtiCompatibility)
    {
        mbtiCompatibility.setUpdateTime(DateUtils.getNowDate());
        return mbtiCompatibilityMapper.updateMbtiCompatibility(mbtiCompatibility);
    }

    /**
     * 批量删除MBTI类型兼容性
     *
     * @param compatibilityIds 需要删除的MBTI类型兼容性主键
     * @return 结果
     */
    @Override
    public int deleteMbtiCompatibilityByCompatibilityIds(Long[] compatibilityIds)
    {
        return mbtiCompatibilityMapper.deleteMbtiCompatibilityByCompatibilityIds(compatibilityIds);
    }

    /**
     * 删除MBTI类型兼容性信息
     *
     * @param compatibilityId MBTI类型兼容性主键
     * @return 结果
     */
    @Override
    public int deleteMbtiCompatibilityByCompatibilityId(Long compatibilityId)
    {
        return mbtiCompatibilityMapper.deleteMbtiCompatibilityByCompatibilityId(compatibilityId);
    }
}
