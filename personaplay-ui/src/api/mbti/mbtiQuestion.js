import request from '@/utils/request'

// 查询MBTI测试问题列表
export function listMbtiQuestion(query) {
  return request({
    url: '/mbti/mbtiQuestion/list',
    method: 'get',
    params: query
  })
}

// 查询MBTI测试问题详细
export function getMbtiQuestion(questionId) {
  return request({
    url: '/mbti/mbtiQuestion/' + questionId,
    method: 'get'
  })
}

// 新增MBTI测试问题
export function addMbtiQuestion(data) {
  return request({
    url: '/mbti/mbtiQuestion',
    method: 'post',
    data: data
  })
}

// 修改MBTI测试问题
export function updateMbtiQuestion(data) {
  return request({
    url: '/mbti/mbtiQuestion',
    method: 'put',
    data: data
  })
}

// 删除MBTI测试问题
export function delMbtiQuestion(questionId) {
  return request({
    url: '/mbti/mbtiQuestion/' + questionId,
    method: 'delete'
  })
}
