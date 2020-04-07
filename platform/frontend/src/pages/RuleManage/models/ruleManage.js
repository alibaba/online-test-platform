import pathToRegexp from 'path-to-regexp';
import { queryRule, addRule, updateRule, removeRule, sendRequest, sendRuleRequest } from '@/services/rule';

export default {
  namespace: 'rule',

  state: {
    data: {
      list: [],
      pagination: {},
    },
    response: "",
    ruleResponse: "",
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
      const response = yield call(queryRule, params);
      yield put({
        type: 'save',
        payload: response,
      });
    },
    *add({ payload, callback }, { call, put }) {
      yield call(addRule, payload);
      yield put({
        type: 'fetch',
        payload: {},
      });
      if (callback) callback();
    },
    *update({ payload, callback }, { call, put }) {
      yield call(updateRule, payload);
      yield put({
        type: 'fetch',
        payload: {},
      });
      if (callback) callback();
    },
    *remove({ payload: id, callback }, { call, put }) {
      yield call(removeRule, { id });
      yield put({
        type: 'fetch',
        payload: {},
      });
      if (callback) callback();
    },
    *sendRequest({ payload, callback }, { call, put }) {
      const res = yield call(sendRequest, {url: payload});
      yield put({
        type: 'saveResponse',
        payload: res,
      });
      if (callback) callback();
    },
    *sendRuleRequest({ payload: { url, data }, callback }, { call, put }) {
      const res = yield call(sendRuleRequest, { url, data });
      yield put({
        type: 'saveRuleResponse',
        payload: res,
      });
      if (callback) callback();
    },
    *clearDebug({ payload: { url, data }, callback }, { put }) {
      yield put({
        type: 'saveResponse',
        payload: { data: "" },
      });
      yield put({
        type: 'saveRuleResponse',
        payload: { data: "" },
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
    saveResponse(state, { payload }) {
      return {
        ...state,
        response: payload.data,
      };
    },
    saveRuleResponse(state, { payload }) {
      return {
        ...state,
        ruleResponse: payload.data,
      };
    },
  },

  subscriptions: {
    subscriber({ dispatch, history }) {
      return history.listen(({ pathname }) => {
        const match1 = pathToRegexp(`/ruleManage`).exec(pathname);
        if (match1) {
          dispatch({ type: 'fetch', payload: {} });
        }
      });
    },
  },
};
