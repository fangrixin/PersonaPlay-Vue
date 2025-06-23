import request from '@/utils/request'

// 查询房间成员列表
export function listMbtiRoomMember(query) {
  return request({
    url: '/mbti/mbtiRoomMember/list',
    method: 'get',
    params: query
  })
}

// 查询房间成员详细
export function getMbtiRoomMember(memberId) {
  return request({
    url: '/mbti/mbtiRoomMember/' + memberId,
    method: 'get'
  })
}

// 新增房间成员
export function addMbtiRoomMember(data) {
  return request({
    url: '/mbti/mbtiRoomMember',
    method: 'post',
    data: data
  })
}

// 修改房间成员
export function updateMbtiRoomMember(data) {
  return request({
    url: '/mbti/mbtiRoomMember',
    method: 'put',
    data: data
  })
}

// 删除房间成员
export function delMbtiRoomMember(memberId) {
  return request({
    url: '/mbti/mbtiRoomMember/' + memberId,
    method: 'delete'
  })
}
