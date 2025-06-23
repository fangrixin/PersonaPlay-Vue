import request from '@/utils/request'

// 查询用户关系列表
export function listMbtiUserRelationship(query) {
  return request({
    url: '/mbti/mbtiUserRelationship/list',
    method: 'get',
    params: query
  })
}

// 查询用户关系详细
export function getMbtiUserRelationship(relationshipId) {
  return request({
    url: '/mbti/mbtiUserRelationship/' + relationshipId,
    method: 'get'
  })
}

// 新增用户关系
export function addMbtiUserRelationship(data) {
  return request({
    url: '/mbti/mbtiUserRelationship',
    method: 'post',
    data: data
  })
}

// 修改用户关系
export function updateMbtiUserRelationship(data) {
  return request({
    url: '/mbti/mbtiUserRelationship',
    method: 'put',
    data: data
  })
}

// 删除用户关系
export function delMbtiUserRelationship(relationshipId) {
  return request({
    url: '/mbti/mbtiUserRelationship/' + relationshipId,
    method: 'delete'
  })
}
