import React, {PureComponent, Fragment} from 'react';
import {connect} from 'dva';
import Link from 'umi/link';
import {Card, Form, Table, Drawer, Badge, Divider } from 'antd';
import PageHeaderWrapper from '@/components/PageHeaderWrapper';
import ReactJson from 'react-json-view';
import { isJson } from '@/utils/utils';
import styles from './index.less';

const getValue = obj =>
  Object.keys(obj)
    .map(key => obj[key])
    .join(',');

/* eslint react/no-multi-comp:0 */
@connect(({ caseDetail, loading }) => ({
  caseDetail,
  loading: loading.models.caseDetail,
}))
@Form.create()
class CaseDetail extends PureComponent {
  state = {
    formValues: {},
    drawerVisible: false,
    verifyRuleList: [],
    traceVisible: false,
    traceInfo: {},
  };

  componentDidMount() {
    const { dispatch, match } = this.props;
    const { params } = match;
    dispatch({
      type: 'caseDetail/fetch',
      payload: { taskId: params.id, caseIds: params.caseIds },
    });
  }

  handleStandardTableChange = (pagination, filtersArg, sorter) => {
    const { dispatch } = this.props;
    const { formValues } = this.state;

    const filters = Object.keys(filtersArg).reduce((obj, key) => {
      const newObj = { ...obj };
      newObj[key] = getValue(filtersArg[key]);
      return newObj;
    }, {});

    const params = {
      currentPage: pagination.current,
      pageSize: pagination.pageSize,
      ...formValues,
      ...filters,
    };
    if (sorter.field) {
      params.sorter = `${sorter.field}_${sorter.order}`;
    }

    dispatch({
      type: 'caseDetail/fetch',
      payload: params,
    });
  };

  handleDrawerVisible = (visible, verifyRuleResult) => {
    this.setState({
      drawerVisible: !!visible,
      verifyRuleList: verifyRuleResult && verifyRuleResult.data && verifyRuleResult.data.executedRuleSet ? verifyRuleResult.data.executedRuleSet : [],
    });
  };

  handleTraceVisible = (visible, verifyRuleResult) => {
    this.setState({
      traceVisible: !!visible,
      traceInfo: verifyRuleResult && verifyRuleResult.debugMap ? verifyRuleResult.debugMap : [],
    });
  };

  render() {
    const {
      caseDetail: { data },
      loading,
    } = this.props;

    const { drawerVisible, verifyRuleList, traceVisible, traceInfo } = this.state;

    const columns = [
      {
        title: '任务id',
        dataIndex: 'taskid',
        width: '10%',
      },
      {
        title: '用例id',
        dataIndex: 'caseid',
        width: '10%',
      },
      {
        title: '请求',
        dataIndex: 'request',
      },
      {
        title: '结果',
        dataIndex: 'response',
        render: val => isJson(val) ? <ReactJson collapsed={true} src={JSON.parse(val)} /> : <span>{val}</span>
      },
      {
        title: '规则结果',
        width: '10%',
        render: (text, record) => <a onClick={() => this.handleDrawerVisible(true, record.ruleResult && record.ruleResult !== '' ? JSON.parse(record.ruleResult) : {})}>详情</a>,
      },
      {
        title: '操作',
        width: '12%',
        render: (text, record) => (
          <Fragment>
            <a onClick={() => this.handleTraceVisible(true, record.ruleResult && record.ruleResult !== '' ? JSON.parse(record.ruleResult) : {})}>trace</a>
            <Divider type="vertical" />
            <Link to={{pathname: '/ruleDebug',query: { request: record.request }}}>debug</Link>
          </Fragment>
        ),
      },
    ];

    const drawerColumns =[
      {
        title: '规则名称',
        dataIndex: 'name',
        width: '20%',
      },
      {
        title: '状态',
        dataIndex: 'state',
        width: '15%',
        render: val => <Badge status={val === 'FAIL' ? 'error' : val === 'PASS' ? 'success' : 'default'} text={val} />,

      },
      {
        title: '规则类型',
        dataIndex: 'category',
        width: '20%',
      },
      {
        title: '错误信息',
        dataIndex: 'message',
      },
    ];

    return (
      <PageHeaderWrapper>
        <div className={styles.standardList}>
          <Card title="命中请求" bordered={false} bodyStyle={{ padding: '0 24px 24px 24px' }}>
            <div className={styles.tableList}>
              <Table
                rowKey="id"
                loading={loading}
                dataSource={data.list}
                bordered
                columns={columns}
                pagination={{ showSizeChanger: true, showQuickJumper: true, ...data.pagination }}
                onChange={this.handleStandardTableChange}
              />
            </div>
          </Card>
        </div>

        <Drawer
          title="规则验证结果"
          placement="right"
          width={800}
          closable={false}
          onClose={() => this.handleDrawerVisible()}
          visible={drawerVisible}
        >
          <Table
            rowKey="id"
            dataSource={verifyRuleList}
            bordered
            columns={drawerColumns}
            pagination={{ showSizeChanger: true, showQuickJumper: true }}
          />
        </Drawer>

        <Drawer
          title="trace信息"
          placement="right"
          width={800}
          closable={false}
          onClose={() => this.handleTraceVisible()}
          visible={traceVisible}
        >
          <ReactJson src={traceInfo} />
        </Drawer>
      </PageHeaderWrapper>
    );
  }
}

export default CaseDetail;
