package com.personaplay.mbti.service.impl;

import java.util.List;
import com.personaplay.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.personaplay.mbti.mapper.MbtiUserMapper;
import com.personaplay.mbti.domain.MbtiUser;
import com.personaplay.mbti.service.IMbtiUserService;

/**
 * MBTI用户信息Service业务层处理
 *
 * @author fangrx
 * @date 2025-03-24
 */
@Service
public class MbtiUserServiceImpl implements IMbtiUserService
{
    @Autowired
    private MbtiUserMapper mbtiUserMapper;

    /**
     * 查询MBTI用户信息
     *
     * @param userId MBTI用户信息主键
     * @return MBTI用户信息
     */
    @Override
    public MbtiUser selectMbtiUserByUserId(Long userId)
    {
        return mbtiUserMapper.selectMbtiUserByUserId(userId);
    }

    /**
     * 查询MBTI用户信息列表
     *
     * @param mbtiUser MBTI用户信息
     * @return MBTI用户信息
     */
    @Override
    public List<MbtiUser> selectMbtiUserList(MbtiUser mbtiUser)
    {
        return mbtiUserMapper.selectMbtiUserList(mbtiUser);
    }

    /**
     * 新增MBTI用户信息
     *
     * @param mbtiUser MBTI用户信息
     * @return 结果
     */
    @Override
    public int insertMbtiUser(MbtiUser mbtiUser)
    {
        mbtiUser.setCreateTime(DateUtils.getNowDate());
        return mbtiUserMapper.insertMbtiUser(mbtiUser);
    }

    /**
     * 修改MBTI用户信息
     *
     * @param mbtiUser MBTI用户信息
     * @return 结果
     */
    @Override
    public int updateMbtiUser(MbtiUser mbtiUser)
    {
        mbtiUser.setUpdateTime(DateUtils.getNowDate());
        return mbtiUserMapper.updateMbtiUser(mbtiUser);
    }

    /**
     * 批量删除MBTI用户信息
     *
     * @param userIds 需要删除的MBTI用户信息主键
     * @return 结果
     */
    @Override
    public int deleteMbtiUserByUserIds(Long[] userIds)
    {
        return mbtiUserMapper.deleteMbtiUserByUserIds(userIds);
    }

    /**
     * 删除MBTI用户信息信息
     *
     * @param userId MBTI用户信息主键
     * @return 结果
     */
    @Override
    public int deleteMbtiUserByUserId(Long userId)
    {
        return mbtiUserMapper.deleteMbtiUserByUserId(userId);
    }

    @Override
    public MbtiUser selectMbtiUserByOpenID(String openid) {
        return mbtiUserMapper.selectMbtiUserByOpenID(openid);
    }
}
