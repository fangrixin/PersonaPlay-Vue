import request from '@/utils/request'

// 查询测试答题记录列表
export function listMbtiTestAnswer(query) {
  return request({
    url: '/mbti/mbtiTestAnswer/list',
    method: 'get',
    params: query
  })
}

// 查询测试答题记录详细
export function getMbtiTestAnswer(answerId) {
  return request({
    url: '/mbti/mbtiTestAnswer/' + answerId,
    method: 'get'
  })
}

// 新增测试答题记录
export function addMbtiTestAnswer(data) {
  return request({
    url: '/mbti/mbtiTestAnswer',
    method: 'post',
    data: data
  })
}

// 修改测试答题记录
export function updateMbtiTestAnswer(data) {
  return request({
    url: '/mbti/mbtiTestAnswer',
    method: 'put',
    data: data
  })
}

// 删除测试答题记录
export function delMbtiTestAnswer(answerId) {
  return request({
    url: '/mbti/mbtiTestAnswer/' + answerId,
    method: 'delete'
  })
}
