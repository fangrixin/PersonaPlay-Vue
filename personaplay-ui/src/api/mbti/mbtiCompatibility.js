import request from '@/utils/request'

// 查询MBTI类型兼容性列表
export function listMbtiCompatibility(query) {
  return request({
    url: '/mbti/mbtiCompatibility/list',
    method: 'get',
    params: query
  })
}

// 查询MBTI类型兼容性详细
export function getMbtiCompatibility(compatibilityId) {
  return request({
    url: '/mbti/mbtiCompatibility/' + compatibilityId,
    method: 'get'
  })
}

// 新增MBTI类型兼容性
export function addMbtiCompatibility(data) {
  return request({
    url: '/mbti/mbtiCompatibility',
    method: 'post',
    data: data
  })
}

// 修改MBTI类型兼容性
export function updateMbtiCompatibility(data) {
  return request({
    url: '/mbti/mbtiCompatibility',
    method: 'put',
    data: data
  })
}

// 删除MBTI类型兼容性
export function delMbtiCompatibility(compatibilityId) {
  return request({
    url: '/mbti/mbtiCompatibility/' + compatibilityId,
    method: 'delete'
  })
}
