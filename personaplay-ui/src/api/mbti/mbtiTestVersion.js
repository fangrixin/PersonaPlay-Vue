import request from '@/utils/request'

// 查询MBTI测试版本列表
export function listMbtiTestVersion(query) {
  return request({
    url: '/mbti/mbtiTestVersion/list',
    method: 'get',
    params: query
  })
}

// 查询MBTI测试版本详细
export function getMbtiTestVersion(versionId) {
  return request({
    url: '/mbti/mbtiTestVersion/' + versionId,
    method: 'get'
  })
}

// 新增MBTI测试版本
export function addMbtiTestVersion(data) {
  return request({
    url: '/mbti/mbtiTestVersion',
    method: 'post',
    data: data
  })
}

// 修改MBTI测试版本
export function updateMbtiTestVersion(data) {
  return request({
    url: '/mbti/mbtiTestVersion',
    method: 'put',
    data: data
  })
}

// 删除MBTI测试版本
export function delMbtiTestVersion(versionId) {
  return request({
    url: '/mbti/mbtiTestVersion/' + versionId,
    method: 'delete'
  })
}
