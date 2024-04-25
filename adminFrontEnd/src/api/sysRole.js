import request from '@/utils/request'

const base_api="/admin/system/sysRole"
// 分页查询角色数据
export const GetSysRoleListByPage = (current , limit , queryDto) => {
  return request({
    url: `${base_api}/findByPage/${current}/${limit}`,
    method: 'post',
    data: queryDto,
  })
}

// 添加角色请求方法
export const SaveSysRole = (data) => {
  return request({
    url: `${base_api}/saveSysRole`,
    method: 'post',
    data,
  })
}

// 保存修改
export const UpdateSysRole = (data) => {
  return request({
    url: `${base_api}/updateSysRole`,
    method: 'put',
    data,
  })
}

// 删除角色
export const DeleteSysRoleById = (roleId) => {
  return request({
    url: `${base_api}/deleteById/${roleId}`,
    method: 'delete',
  })
}

// 查询所有的角色数据
export const GetAllRoleList = (userId) => {
  return request({
    url: `${base_api}/findAllRoles/${userId}`,
    method: 'get',
  })
}

// 查询指定角色所对应的菜单id
export const GetSysRoleMenuIds = (roleId) => {
  return request({
    url: "/admin/system/sysRoleMenu/findSysRoleMenuByRoleId/"+ roleId,
    method: 'get'
  })
}
// 根据角色分配菜单请求方法
export const DoAssignMenuIdToSysRole = (assignMenuDto) => {
  return request({
    url: "/admin/system/sysRoleMenu/doAssign",
    method: 'post',
    data: assignMenuDto
  })
}