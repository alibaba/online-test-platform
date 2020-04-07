import React, { Fragment, PureComponent } from 'react';
import { connect } from 'dva';
import { Button, Card, Divider, Form, Input, message, Modal, Table, Select, Badge } from 'antd';
import PageHeaderWrapper from '@/components/PageHeaderWrapper';

import styles from './index.less';

const { TextArea } = Input;
const { Option } = Select;
const FormItem = Form.Item;
const getValue = obj =>
  Object.keys(obj)
    .map(key => obj[key])
    .join(',');

const CreateForm = Form.create()(props => {
  const { current, modalVisible, form, handleAdd, handleUpdate, handleModalVisible } = props;
  const okHandle = () => {
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      form.resetFields();
      if (current && current.id) {
        handleUpdate({ ...fieldsValue, id: current.id });
      } else {
        handleAdd(fieldsValue);
      }
    });
  };
  return (
    <Modal
      width={800}
      destroyOnClose
      title={current && current.id ? '编辑规则' : '新建规则'}
      visible={modalVisible}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >
      <FormItem labelCol={{ span: 6 }} wrapperCol={{ span: 17 }} label="应用名称">
        {form.getFieldDecorator('application', {
          rules: [{ required: true, message: '请输入应用名称！' }],
          initialValue: current.application,
        })(<Input placeholder="请输入" />)}
      </FormItem>
      <FormItem labelCol={{ span: 6 }} wrapperCol={{ span: 17 }} label="规则名称">
        {form.getFieldDecorator('name', {
          rules: [{ required: true, message: '请输入规则名称！' }],
          initialValue: current.name,
        })(<Input placeholder="请输入" />)}
      </FormItem>
      <FormItem labelCol={{ span: 6 }} wrapperCol={{ span: 17 }} label="规则分类">
        {form.getFieldDecorator('category', {
          rules: [{ required: true, message: '请选择规则分类！' }],
          initialValue: current.category,
        })(
          <Select placeholder="请选择" style={{ width: '100%' }}>
            <Option value="字段取值验证">字段取值验证</Option>
            <Option value="功能逻辑验证">功能逻辑验证</Option>
            <Option value="异常验证">异常验证</Option>
            <Option value="其他验证">其他验证</Option>
          </Select>
        )}
      </FormItem>
      <FormItem labelCol={{ span: 6 }} wrapperCol={{ span: 17 }} label="规则等级">
        {form.getFieldDecorator('level', {
          rules: [{ required: true, message: '请选择规则等级！' }],
          initialValue: current.level,
        })(
          <Select placeholder="请选择" style={{ width: '100%' }}>
            <Option value={1}>高</Option>
            <Option value={2}>中</Option>
            <Option value={3}>低</Option>
          </Select>
        )}
      </FormItem>
      <FormItem labelCol={{ span: 6 }} wrapperCol={{ span: 17 }} label="是否启用">
        {form.getFieldDecorator('enable', {
          rules: [{ required: true, message: '请选择是否启用！' }],
          initialValue: current.enable,
        })(
          <Select placeholder="请选择" style={{ width: '100%' }}>
            <Option value={0}>否</Option>
            <Option value={1}>是</Option>
          </Select>
        )}
      </FormItem>
      <FormItem labelCol={{ span: 6 }} wrapperCol={{ span: 17 }} label="准入条件">
        {form.getFieldDecorator('rWhen', {
          rules: [{ required: true, message: '请输入准入条件！' }],
          initialValue: current.rWhen,
        })(<TextArea placeholder="请输入" autosize={{ minRows: 8 }} />)}
      </FormItem>
      <FormItem labelCol={{ span: 6 }} wrapperCol={{ span: 17 }} label="规则内容">
        {form.getFieldDecorator('rVerify', {
          rules: [{ required: true, message: '请输入规则内容！' }],
          initialValue: current.rVerify,
        })(<TextArea placeholder="请输入" autosize={{ minRows: 8 }} />)}
      </FormItem>
    </Modal>
  );
});

/* eslint react/no-multi-comp:0 */
@connect(({ rule, loading }) => ({
  rule,
  loading: loading.models.rule,
}))
@Form.create()
class RuleManage extends PureComponent {
  state = {
    current: {},
    modalVisible: false,
    formValues: {},
  };

  columns = [
    {
      title: '规则id',
      dataIndex: 'id',
      width: '8%',
    },
    {
      title: '规则名称',
      dataIndex: 'name',
      width: '10%',
    },
    {
      title: '分类',
      dataIndex: 'category',
    },
    {
      title: '级别',
      dataIndex: 'level',
      width: '8%',
      render: level => <span>{level === 1 ? '高' : level === 2 ? '中' : '低'}</span>,
    },
    {
      title: '准入条件',
      dataIndex: 'rWhen',
    },
    {
      title: '规则内容',
      dataIndex: 'rVerify',
    },
    {
      title: '状态',
      dataIndex: 'enable',
      width: '8%',
      render(val) {
        return (
          <Badge status={val === 1 ? 'success' : 'default'} text={val === 1 ? '启用' : '禁用'} />
        );
      },
    },
    {
      title: '应用',
      dataIndex: 'application',
    },
    {
      title: '操作',
      width: '10%',
      render: (text, record) => (
        <Fragment>
          <a onClick={() => this.handleDelete(record.id)}>删除</a>
          <Divider type="vertical" />
          <a onClick={() => this.handleModalVisible(true, record)}>编辑</a>
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
      type: 'rule/fetch',
      payload: params,
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
      type: 'rule/add',
      payload: fields,
    });

    message.success('添加成功');
    this.handleModalVisible();
  };

  handleUpdate = fields => {
    const { dispatch } = this.props;
    dispatch({
      type: 'rule/update',
      payload: fields,
    });

    message.success('编辑成功');
    this.handleModalVisible();
  };

  handleDelete = id => {
    const { dispatch } = this.props;
    dispatch({
      type: 'rule/remove',
      payload: id,
    });
    message.success('删除成功');
  };

  render() {
    const {
      rule: { data },
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

export default RuleManage;
