import React, { Fragment, PureComponent } from 'react';
import { connect } from 'dva';
import Link from 'umi/link';
import moment from 'moment';
import { Badge, Button, Card, Divider, Form, Input, message, Modal, Table } from 'antd';
import PageHeaderWrapper from '@/components/PageHeaderWrapper';

import styles from './index.less';

const FormItem = Form.Item;
const getValue = obj =>
  Object.keys(obj)
    .map(key => obj[key])
    .join(',');
const statusMap = ['processing', 'success', 'error'];
const status = ['运行中', '运行成功', '运行失败'];

const CreateForm = Form.create()(props => {
  const { current, modalVisible, form, handleAdd, handleUpdate, handleModalVisible } = props;
  const okHandle = () => {
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      form.resetFields();
      if (current && current.id) {
        handleUpdate({ ...fieldsValue, id: current.id });
      }
      handleAdd(fieldsValue);
    });
  };
  return (
    <Modal
      destroyOnClose
      title={current && current.id ? '编辑任务' : '新建任务'}
      visible={modalVisible}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >
      <FormItem labelCol={{ span: 6 }} wrapperCol={{ span: 15 }} label="任务名称">
        {form.getFieldDecorator('taskInfo', {
          rules: [{ required: true, message: '请输入任务名称！' }],
          initialValue: current.taskInfo,
        })(<Input placeholder="请输入" />)}
      </FormItem>
      <FormItem labelCol={{ span: 6 }} wrapperCol={{ span: 15 }} label="测试地址">
        {form.getFieldDecorator('host', {
          rules: [{ required: true, message: '请输入机器IP！' }],
          initialValue: current.host,
        })(<Input placeholder="请输入" />)}
      </FormItem>
      <FormItem labelCol={{ span: 6 }} wrapperCol={{ span: 15 }} label="测试数据路径">
        {form.getFieldDecorator('testData', {
          rules: [{ required: true, message: '请输入测试数据路径！' }],
          initialValue: current.testData,
        })(<Input placeholder="请输入" />)}
      </FormItem>
    </Modal>
  );
});

/* eslint react/no-multi-comp:0 */
@connect(({ task, loading }) => ({
  task,
  loading: loading.models.task,
}))
@Form.create()
class TaskManage extends PureComponent {
  state = {
    current: {},
    modalVisible: false,
    formValues: {},
  };

  columns = [
    {
      title: '任务id',
      dataIndex: 'id',
    },
    {
      title: '任务名称',
      dataIndex: 'taskInfo',
    },
    {
      title: '状态',
      dataIndex: 'taskResult',
      filters: [
        {
          text: status[0],
          value: 0,
        },
        {
          text: status[1],
          value: 1,
        },
        {
          text: status[2],
          value: 2,
        },
      ],
      render(val) {
        return <Badge status={statusMap[val]} text={status[val]} />;
      },
    },
    {
      title: '测试地址',
      dataIndex: 'host',
    },
    {
      title: '测试数据',
      dataIndex: 'testData',
    },
    {
      title: '开始时间',
      dataIndex: 'startTime',
      render: val => <span>{val ? moment(val).format('YYYY-MM-DD HH:mm:ss') : undefined}</span>,
    },
    {
      title: '结束时间',
      dataIndex: 'endTime',
      render: val => <span>{val ? moment(val).format('YYYY-MM-DD HH:mm:ss') : undefined}</span>,
    },
    {
      title: '操作',
      render: (text, record) => (
        <Fragment>
          <a onClick={() => this.handleDelete(record.id)}>删除</a>
          <Divider type="vertical" />
          <Link to={`/taskDetail/${record.id}`}>详情</Link>
        </Fragment>
      ),
    },
  ];

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
      type: 'task/fetch',
      payload: params,
    });
  };

  handleFormReset = () => {
    const { form, dispatch } = this.props;
    form.resetFields();
    this.setState({
      formValues: {},
    });
    dispatch({
      type: 'task/fetch',
      payload: {},
    });
  };

  handleSearch = e => {
    e.preventDefault();

    const { dispatch, form } = this.props;

    form.validateFields((err, fieldsValue) => {
      if (err) return;

      const values = {
        ...fieldsValue,
        updatedAt: fieldsValue.updatedAt && fieldsValue.updatedAt.valueOf(),
      };

      this.setState({
        formValues: values,
      });

      dispatch({
        type: 'task/fetch',
        payload: values,
      });
    });
  };

  handleModalVisible = (flag, record) => {
    this.setState({
      modalVisible: !!flag,
      current: record || {},
    });
  };

  handleAdd = fields => {
    const { dispatch } = this.props;
    dispatch({
      type: 'task/add',
      payload: fields,
    });

    message.success('添加成功');
    this.handleModalVisible();
  };

  handleUpdate = fields => {
    const { dispatch } = this.props;
    dispatch({
      type: 'task/update',
      payload: fields,
    });

    message.success('编辑成功');
    this.handleModalVisible();
  };

  handleDelete = id => {
    const { dispatch } = this.props;
    dispatch({
      type: 'task/remove',
      payload: id,
    });
    message.success('删除成功');
  };

  render() {
    const {
      task: { data },
      loading,
    } = this.props;
    const { current, modalVisible } = this.state;

    const parentMethods = {
      handleAdd: this.handleAdd,
      handleUpdate: this.handleUpdate,
      handleModalVisible: this.handleModalVisible,
    };
    return (
      <PageHeaderWrapper>
        <Card bordered={false}>
          <div className={styles.tableList}>
            <div className={styles.tableListOperator}>
              <Button icon="plus" type="primary" onClick={() => this.handleModalVisible(true)}>
                新建
              </Button>
            </div>
            <Table
              rowKey="id"
              loading={loading}
              dataSource={data.list}
              bordered
              columns={this.columns}
              pagination={{ showSizeChanger: true, showQuickJumper: true, ...data.pagination }}
              onChange={this.handleStandardTableChange}
            />
          </div>
        </Card>
        <CreateForm {...parentMethods} current={current} modalVisible={modalVisible} />
      </PageHeaderWrapper>
    );
  }
}

export default TaskManage;
