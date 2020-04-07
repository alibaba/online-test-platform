import request from '@/utils/request';

export async function queryTask(params) {
  return request(
    `/server/api/task/selectAllByPage?pageNum=${params.pageNum}&pageSize=${params.pageSize}`
  );
}

export async function getTask(params) {
  return request(`/server/api/task/getTask?id=${params.id}`);
}

export async function addTask(params) {
  return request(
    `/server/api/task/runTest?comment=${params.taskInfo}&host=${params.host}&data=${params.testData}`
  );
}

export async function removeTask(params) {
  return request(`/server/api/task/deleteTask?id=${params.id}`);
}

export async function queryCase(params) {
  return request(
    `/server/api/task/selectResult?taskId=${params.taskId}&caseIds=${params.caseIds}`
  );
}
