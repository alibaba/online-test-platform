export default [
  // app
  {
    path: '/',
    component: '../layouts/BasicLayout',
    Routes: ['src/pages/Authorized'],
    routes: [
      { path: '/', redirect: '/taskManage' },
      // task manage
      {
        name: 'taskManage',
        icon: 'table',
        path: '/taskManage/',
        component: './TaskManage',
      },
      // task detail
      {
        name: 'taskDetail',
        icon: 'profile',
        path: '/taskDetail/:id',
        hideInMenu: true,
        component: './TaskDetail',
      },
      // case detail
      {
        name: 'caseDetail',
        icon: 'profile',
        path: '/caseDetail/:id/:caseIds',
        hideInMenu: true,
        component: './CaseDetail',
      },
      // rule manage
      {
        name: 'ruleManage',
        icon: 'audit',
        path: '/ruleManage/',
        component: './RuleManage',
      },
      // rule debug
      {
        name: 'ruleDebug',
        icon: 'interaction',
        path: '/ruleDebug/',
        component: './RuleManage/Debug',
      },
      {
        component: '404',
      },
    ],
  },
];
