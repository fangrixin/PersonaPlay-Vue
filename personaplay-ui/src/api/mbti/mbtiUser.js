import request from '@/utils/request'

// 查询MBTI用户信息列表
export function listMbtiUser(query) {
  return request({
    url: '/mbti/mbtiUser/list',
    method: 'get',
    params: query
  })
}

// 查询MBTI用户信息详细
export function getMbtiUser(userId) {
  return request({
    url: '/mbti/mbtiUser/' + userId,
    method: 'get'
  })
}

// 新增MBTI用户信息
export function addMbtiUser(data) {
  return request({
    url: '/mbti/mbtiUser',
    method: 'post',
    data: data
  })
}

// 修改MBTI用户信息
export function updateMbtiUser(data) {
  return request({
    url: '/mbti/mbtiUser',
    method: 'put',
    data: data
  })
}

// 删除MBTI用户信息
export function delMbtiUser(userId) {
  return request({
    url: '/mbti/mbtiUser/' + userId,
    method: 'delete'
  })
}
