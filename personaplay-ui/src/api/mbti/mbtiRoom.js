import request from '@/utils/request'

// 查询MBTI测试房间列表
export function listMbtiRoom(query) {
  return request({
    url: '/mbti/mbtiRoom/list',
    method: 'get',
    params: query
  })
}

// 查询MBTI测试房间详细
export function getMbtiRoom(roomId) {
  return request({
    url: '/mbti/mbtiRoom/' + roomId,
    method: 'get'
  })
}

// 新增MBTI测试房间
export function addMbtiRoom(data) {
  return request({
    url: '/mbti/mbtiRoom',
    method: 'post',
    data: data
  })
}

// 修改MBTI测试房间
export function updateMbtiRoom(data) {
  return request({
    url: '/mbti/mbtiRoom',
    method: 'put',
    data: data
  })
}

// 删除MBTI测试房间
export function delMbtiRoom(roomId) {
  return request({
    url: '/mbti/mbtiRoom/' + roomId,
    method: 'delete'
  })
}
