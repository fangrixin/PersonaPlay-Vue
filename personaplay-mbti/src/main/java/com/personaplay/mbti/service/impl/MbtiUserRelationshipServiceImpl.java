package com.personaplay.mbti.service.impl;

import java.util.List;
import com.personaplay.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.personaplay.mbti.mapper.MbtiUserRelationshipMapper;
import com.personaplay.mbti.domain.MbtiUserRelationship;
import com.personaplay.mbti.service.IMbtiUserRelationshipService;

/**
 * 用户关系Service业务层处理
 *
 * @author fangrx
 * @date 2025-03-24
 */
@Service
public class MbtiUserRelationshipServiceImpl implements IMbtiUserRelationshipService
{
    @Autowired
    private MbtiUserRelationshipMapper mbtiUserRelationshipMapper;

    /**
     * 查询用户关系
     *
     * @param relationshipId 用户关系主键
     * @return 用户关系
     */
    @Override
    public MbtiUserRelationship selectMbtiUserRelationshipByRelationshipId(Long relationshipId)
    {
        return mbtiUserRelationshipMapper.selectMbtiUserRelationshipByRelationshipId(relationshipId);
    }

    /**
     * 查询用户关系列表
     *
     * @param mbtiUserRelationship 用户关系
     * @return 用户关系
     */
    @Override
    public List<MbtiUserRelationship> selectMbtiUserRelationshipList(MbtiUserRelationship mbtiUserRelationship)
    {
        return mbtiUserRelationshipMapper.selectMbtiUserRelationshipList(mbtiUserRelationship);
    }

    /**
     * 新增用户关系
     *
     * @param mbtiUserRelationship 用户关系
     * @return 结果
     */
    @Override
    public int insertMbtiUserRelationship(MbtiUserRelationship mbtiUserRelationship)
    {
        mbtiUserRelationship.setCreateTime(DateUtils.getNowDate());
        return mbtiUserRelationshipMapper.insertMbtiUserRelationship(mbtiUserRelationship);
    }

    /**
     * 修改用户关系
     *
     * @param mbtiUserRelationship 用户关系
     * @return 结果
     */
    @Override
    public int updateMbtiUserRelationship(MbtiUserRelationship mbtiUserRelationship)
    {
        mbtiUserRelationship.setUpdateTime(DateUtils.getNowDate());
        return mbtiUserRelationshipMapper.updateMbtiUserRelationship(mbtiUserRelationship);
    }

    /**
     * 批量删除用户关系
     *
     * @param relationshipIds 需要删除的用户关系主键
     * @return 结果
     */
    @Override
    public int deleteMbtiUserRelationshipByRelationshipIds(Long[] relationshipIds)
    {
        return mbtiUserRelationshipMapper.deleteMbtiUserRelationshipByRelationshipIds(relationshipIds);
    }

    /**
     * 删除用户关系信息
     *
     * @param relationshipId 用户关系主键
     * @return 结果
     */
    @Override
    public int deleteMbtiUserRelationshipByRelationshipId(Long relationshipId)
    {
        return mbtiUserRelationshipMapper.deleteMbtiUserRelationshipByRelationshipId(relationshipId);
    }
}
