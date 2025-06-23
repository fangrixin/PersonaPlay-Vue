package com.personaplay.mbti.service.impl;

import java.util.List;
import com.personaplay.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.personaplay.mbti.mapper.MbtiPersonalityTypeMapper;
import com.personaplay.mbti.domain.MbtiPersonalityType;
import com.personaplay.mbti.service.IMbtiPersonalityTypeService;

/**
 * MBTI人格类型Service业务层处理
 *
 * @author fangrx
 * @date 2025-03-24
 */
@Service
public class MbtiPersonalityTypeServiceImpl implements IMbtiPersonalityTypeService
{
    @Autowired
    private MbtiPersonalityTypeMapper mbtiPersonalityTypeMapper;

    /**
     * 查询MBTI人格类型
     *
     * @param typeId MBTI人格类型主键
     * @return MBTI人格类型
     */
    @Override
    public MbtiPersonalityType selectMbtiPersonalityTypeByTypeId(Long typeId)
    {
        return mbtiPersonalityTypeMapper.selectMbtiPersonalityTypeByTypeId(typeId);
    }

    /**
     * 查询MBTI人格类型列表
     *
     * @param mbtiPersonalityType MBTI人格类型
     * @return MBTI人格类型
     */
    @Override
    public List<MbtiPersonalityType> selectMbtiPersonalityTypeList(MbtiPersonalityType mbtiPersonalityType)
    {
        return mbtiPersonalityTypeMapper.selectMbtiPersonalityTypeList(mbtiPersonalityType);
    }

    /**
     * 新增MBTI人格类型
     *
     * @param mbtiPersonalityType MBTI人格类型
     * @return 结果
     */
    @Override
    public int insertMbtiPersonalityType(MbtiPersonalityType mbtiPersonalityType)
    {
        mbtiPersonalityType.setCreateTime(DateUtils.getNowDate());
        return mbtiPersonalityTypeMapper.insertMbtiPersonalityType(mbtiPersonalityType);
    }

    /**
     * 修改MBTI人格类型
     *
     * @param mbtiPersonalityType MBTI人格类型
     * @return 结果
     */
    @Override
    public int updateMbtiPersonalityType(MbtiPersonalityType mbtiPersonalityType)
    {
        mbtiPersonalityType.setUpdateTime(DateUtils.getNowDate());
        return mbtiPersonalityTypeMapper.updateMbtiPersonalityType(mbtiPersonalityType);
    }

    /**
     * 批量删除MBTI人格类型
     *
     * @param typeIds 需要删除的MBTI人格类型主键
     * @return 结果
     */
    @Override
    public int deleteMbtiPersonalityTypeByTypeIds(Long[] typeIds)
    {
        return mbtiPersonalityTypeMapper.deleteMbtiPersonalityTypeByTypeIds(typeIds);
    }

    /**
     * 删除MBTI人格类型信息
     *
     * @param typeId MBTI人格类型主键
     * @return 结果
     */
    @Override
    public int deleteMbtiPersonalityTypeByTypeId(Long typeId)
    {
        return mbtiPersonalityTypeMapper.deleteMbtiPersonalityTypeByTypeId(typeId);
    }
}
