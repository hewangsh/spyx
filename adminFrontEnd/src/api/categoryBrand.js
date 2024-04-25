import request from '@/utils/request'

const api_name = '/admin/product/categoryBrand'

// 分页列表
export const GetCategoryBrandPageList = (page, limit, searchObj) => {
  return request({
    url: `${api_name}/${page}/${limit}`,
    method: 'get',
    params: searchObj,
  })
}

// 保存信息
export const SaveCategoryBrand = categoryBrand => {
  return request({
    url: `${api_name}/save`,
    method: 'post',
    data: categoryBrand,
  })
}

// 修改信息
export const UpdateCategoryBrandById = categoryBrand => {
  return request({
    url: `${api_name}/updateById`,
    method: 'put',
    data: categoryBrand,
  })
}

// 根据id删除数据
export const DeleteCategoryBrandById = id => {
  return request({
    url: `${api_name}/deleteById/${id}`,
    method: 'delete',
  })
}

// 根据分类的id获取品牌数据
export const FindBrandByCategoryId = categoryId => {
  return request({
    url: `${api_name}/findBrandByCategoryId/${categoryId}`,
    method: 'get',
  })
}