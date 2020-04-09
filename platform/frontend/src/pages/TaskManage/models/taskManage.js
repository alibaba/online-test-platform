import pathToRegexp from 'path-to-regexp';
import { queryTask, addTask, removeTask } from '@/services/task';

export default {
  namespace: 'task',

  state: {
    data: {
      list: [],
      pagination: {},
    },
  },

  effects: {
    *fetch(
      {
        payload: { currentPage, pageSize },
      },
      { call, put }
    ) {
      const params = {
        pageNum: currentPage || 1,
        pageSize: pageSize || 10,
      };
      const response = yield call(queryTask, params);
      yield put({
        type: 'save',
        payload: response,
      });
    },
    *add({ payload, callback }, { call, put }) {
      yield call(addTask, payload);
      yield put({
        type: 'fetch',
        payload: {},
      });
      if (callback) callback();
    },
    *remove({ payload: id, callback }, { call, put }) {
      yield call(removeTask, { id });
      yield put({
        type: 'fetch',
        payload: {},
      });
      if (callback) callback();
    },
  },

  reducers: {
    save(state, { payload }) {
      const pagination = {
        current: payload.pageNum,
        pageSize: payload.pageSize,
        total: payload.total,
      };
      const data = {
        ...state.data,
        list: payload.list,
        pagination,
      };
      return {
        ...state,
        data,
      };
    },
  },

  subscriptions: {
    subscriber({ dispatch, history }) {
      return history.listen(({ pathname }) => {
        const match1 = pathToRegexp(`/taskManage`).exec(pathname);
        if (match1) {
          dispatch({ type: 'fetch', payload: {} });
        }
      });
    },
  },
};
