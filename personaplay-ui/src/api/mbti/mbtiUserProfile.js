import request from '@/utils/request'

// 查询用户MBTI档案列表
export function listMbtiUserProfile(query) {
  return request({
    url: '/mbti/mbtiUserProfile/list',
    method: 'get',
    params: query
  })
}

// 查询用户MBTI档案详细
export function getMbtiUserProfile(profileId) {
  return request({
    url: '/mbti/mbtiUserProfile/' + profileId,
    method: 'get'
  })
}

// 新增用户MBTI档案
export function addMbtiUserProfile(data) {
  return request({
    url: '/mbti/mbtiUserProfile',
    method: 'post',
    data: data
  })
}

// 修改用户MBTI档案
export function updateMbtiUserProfile(data) {
  return request({
    url: '/mbti/mbtiUserProfile',
    method: 'put',
    data: data
  })
}

// 删除用户MBTI档案
export function delMbtiUserProfile(profileId) {
  return request({
    url: '/mbti/mbtiUserProfile/' + profileId,
    method: 'delete'
  })
}
