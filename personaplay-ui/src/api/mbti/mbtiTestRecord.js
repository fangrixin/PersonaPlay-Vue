import request from '@/utils/request'

// 查询MBTI测试记录列表
export function listMbtiTestRecord(query) {
  return request({
    url: '/mbti/mbtiTestRecord/list',
    method: 'get',
    params: query
  })
}

// 查询MBTI测试记录详细
export function getMbtiTestRecord(recordId) {
  return request({
    url: '/mbti/mbtiTestRecord/' + recordId,
    method: 'get'
  })
}

// 新增MBTI测试记录
export function addMbtiTestRecord(data) {
  return request({
    url: '/mbti/mbtiTestRecord',
    method: 'post',
    data: data
  })
}

// 修改MBTI测试记录
export function updateMbtiTestRecord(data) {
  return request({
    url: '/mbti/mbtiTestRecord',
    method: 'put',
    data: data
  })
}

// 删除MBTI测试记录
export function delMbtiTestRecord(recordId) {
  return request({
    url: '/mbti/mbtiTestRecord/' + recordId,
    method: 'delete'
  })
}
