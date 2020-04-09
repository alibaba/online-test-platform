import request from '@/utils/request';

export async function queryRule(params) {
  return request(
    `/server/api/rule/selectAllByPage?pageNum=${params.pageNum}&pageSize=${params.pageSize}`
  );
}

export async function addRule(params) {
  return request('/server/api/rule/addRule', {
    method: 'POST',
    data: {
      ...params,
      method: 'post',
    },
  });
}

export async function updateRule(params) {
  return request('/server/api/rule/editRule', {
    method: 'POST',
    data: {
      ...params,
      method: 'update',
    },
  });
}

export async function removeRule(params) {
  return request(`/server/api/rule/deleteRule?ruleId=${params.id}`);
}

export async function sendRequest(params) {
  return request('/server/api/rule/sendRequest', {
    method: 'POST',
    data: {
      ...params,
      method: 'post',
    },
  });
}

export async function sendRuleRequest(params) {
  return request('/server/api/rule/sendRuleRequest', {
    method: 'POST',
    data: {
      ...params,
      method: 'post',
    },
  });
}
