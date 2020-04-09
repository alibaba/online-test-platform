import { queryCase } from '@/services/task';

export default {
  namespace: 'caseDetail',

  state: {
    data: {
      list: [],
    },
  },

  effects: {
    *fetch({ payload: { taskId, caseIds } }, { call, put }) {
      const response = yield call(queryCase, { taskId, caseIds });
      yield put({
        type: 'save',
        payload: response,
      });
    },
  },

  reducers: {
    save(state, { payload }) {
      const data = {
        ...state.data,
        list: payload.list,
      };
      return {
        ...state,
        data,
      };
    },
  },
};
