import request from '@/utils/request'

// 查询MBTI人格类型列表
export function listMbtiPersonalityType(query) {
  return request({
    url: '/mbti/mbtiPersonalityType/list',
    method: 'get',
    params: query
  })
}

// 查询MBTI人格类型详细
export function getMbtiPersonalityType(typeId) {
  return request({
    url: '/mbti/mbtiPersonalityType/' + typeId,
    method: 'get'
  })
}

// 新增MBTI人格类型
export function addMbtiPersonalityType(data) {
  return request({
    url: '/mbti/mbtiPersonalityType',
    method: 'post',
    data: data
  })
}

// 修改MBTI人格类型
export function updateMbtiPersonalityType(data) {
  return request({
    url: '/mbti/mbtiPersonalityType',
    method: 'put',
    data: data
  })
}

// 删除MBTI人格类型
export function delMbtiPersonalityType(typeId) {
  return request({
    url: '/mbti/mbtiPersonalityType/' + typeId,
    method: 'delete'
  })
}
