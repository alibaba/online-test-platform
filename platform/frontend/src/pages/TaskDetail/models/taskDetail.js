import { removeRule, addRule, updateRule } from '@/services/api';
import { getTask } from '@/services/task';

export default {
  namespace: 'taskDetail',

  state: {
    task: {},
    data: {
      list: [],
    },
  },

  effects: {
    *fetch({ payload: {id} }, { call, put }) {
      const response = yield call(getTask, {id});
      yield put({
        type: 'save',
        payload: response,
      });
    },
    *add({ payload, callback }, { call, put }) {
      const response = yield call(addRule, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback();
    },
    *remove({ payload: id, callback }, { call, put }) {
      const response = yield call(removeRule, {id});
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback();
    },
    *update({ payload, callback }, { call, put }) {
      const response = yield call(updateRule, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback();
    },
  },

  reducers: {
    save(state, { payload: task }) {
      let list = [];
      if (task.ruleResult) {
        const ruleResult = JSON.parse(task.ruleResult);
        list = Object.keys(ruleResult.result).map(key => ruleResult.result[key]);
      }
      const data = {
        ...state.data,
        list,
      };
      return {
        ...state,
        task,
        data,
      };
    },
  },
};
