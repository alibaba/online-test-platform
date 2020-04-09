import React, { PureComponent } from 'react';
import { connect } from 'dva';
import Link from 'umi/link';
import { Badge, Button, Card, Col, Form, Row, Select, Table, Tag } from 'antd';
import PageHeaderWrapper from '@/components/PageHeaderWrapper';
import DescriptionList from '@/components/DescriptionList';
import moment from 'moment';
import styles from './index.less';

const FormItem = Form.Item;
const { Option } = Select;
const { Description } = DescriptionList;

const statusMap = ['error', 'success', 'Warning'];
const status = ['FAIL', 'PASS', 'MISS'];

/* eslint react/no-multi-comp:0 */
@connect(({ taskDetail, loading }) => ({
  taskDetail,
  loading: loading.models.taskDetail,
}))
@Form.create()
class TaskDetail extends PureComponent {
  state = {
    formValues: {},
    searchList: [],
  };

  componentDidMount() {
    const { dispatch, match } = this.props;
    const { params } = match;
    dispatch({
      type: 'taskDetail/fetch',
      payload: { id: params.id || '-1' },
    });
  }

  handleFormReset = () => {
    const { form } = this.props;
    form.resetFields();
    this.setState({
      formValues: {},
      searchList: [],
    });
  };

  handleSearch = e => {
    e.preventDefault();
    const {
      form,
      taskDetail: {
        data: { list },
      },
    } = this.props;

    form.validateFields((err, fieldsValue) => {
      if (err) return;
      const values = {
        ...fieldsValue,
      };
      const searchList = list.filter(d => d.finalStatus === fieldsValue.status);
      this.setState({
        formValues: values,
        searchList,
      });
    });
  };

  renderForm() {
    const {
      form: { getFieldDecorator },
    } = this.props;
    return (
      <Form onSubmit={this.handleSearch} layout="inline">
        <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
          <Col md={8} sm={24}>
            <FormItem label="验证状态">
              {getFieldDecorator('status')(
                <Select placeholder="请选择" style={{ width: '100%' }}>
                  <Option value="FAIL">失败</Option>
                  <Option value="PASS">成功</Option>
                  <Option value="MISS">未命中</Option>
                </Select>
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">
                查询
              </Button>
              <Button style={{ marginLeft: 8 }} onClick={this.handleFormReset}>
                重置
              </Button>
            </span>
          </Col>
        </Row>
      </Form>
    );
  }

  render() {
    const {
      taskDetail: { task, data },
      loading,
      match: { params },
    } = this.props;
    const { formValues, searchList } = this.state;

    const columns = [
      {
        title: '规则id',
        dataIndex: 'ruleId',
      },
      {
        title: '规则名称',
        dataIndex: 'ruleName',
      },
      {
        title: '命中次数',
        dataIndex: 'total',
      },
      {
        title: '验证结果',
        dataIndex: 'finalStatus',
        filters: [
          {
            text: status[0],
            value: 0,
          },
          {
            text: status[1],
            value: 1,
          },
        ],
        render(val) {
          return (
            <Badge status={statusMap[val === 'FAIL' ? 0 : val === 'PASS' ? 1 : 2]} text={val} />
          );
        },
      },
      {
        title: '错误信息',
        dataIndex: 'error',
        render: (val, record) =>
          record.finalStatus === 'FAIL' ? (
            <Link to={`/caseDetail/${task.id}/${record.failed.join(',')}`}>{val}</Link>
          ) : null,
      },
    ];

    const Info = ({ title, value, bordered }) => (
      <div className={styles.headerInfo}>
        <span>{title}</span>
        <p>{value}</p>
        {bordered && <em />}
      </div>
    );

    const description = (
      <DescriptionList className={styles.headerList} size="small" col="2">
        <Description term="任务id">{params.id}</Description>
        <Description term="任务名称">{task.taskInfo}</Description>
        <Description term="测试地址">{task.host}</Description>
        <Description term="测试数据">{task.testData}</Description>
        <Description term="执行时间">
          {moment(task.startTime).format('YYYY-MM-DD HH:mm:ss')}
        </Description>
      </DescriptionList>
    );

    let passCount = 0;
    let failCount = 0;
    let missCount = 0;
    data.list.forEach(d => {
      if (d.finalStatus === 'PASS') {
        passCount = passCount + 1;
      } else if (d.finalStatus === 'FAIL') {
        failCount = failCount + 1;
      } else if (d.finalStatus === 'MISS') {
        missCount = missCount + 1;
      }
    });

    return (
      <PageHeaderWrapper
        title={
          <Tag
            style={{ fontSize: 18, padding: '6px 6px 6px 6px' }}
            color={
              task.taskResult === 1 ? '#87d068' : task.taskResult === 0 ? '#108ee9' : '#ff0000'
            }
          >
            {task.taskResult === 1 ? '成功' : task.taskResult === 0 ? '运行中' : '失败'}
          </Tag>
        }
        content={description}
      >
        <div className={styles.standardList}>
          <Card bordered={false} style={{ marginTop: 24 }}>
            <Row>
              <Col sm={8} xs={24}>
                <Info title="验证通过" value={passCount} bordered />
              </Col>
              <Col sm={8} xs={24}>
                <Info title="验证未通过" value={failCount} bordered />
              </Col>
              <Col sm={8} xs={24}>
                <Info title="未命中" value={missCount} />
              </Col>
            </Row>
          </Card>

          <Card
            title="命中规则"
            bordered={false}
            style={{ marginTop: 24 }}
            bodyStyle={{ padding: '0 24px 24px 24px' }}
          >
            <div className={styles.tableList}>
              <div className={styles.tableListForm}>{this.renderForm()}</div>

              <Table
                rowKey="ruleId"
                loading={loading}
                dataSource={formValues.status ? searchList : data.list}
                bordered
                columns={columns}
                pagination={{ showSizeChanger: true, showQuickJumper: true }}
              />
            </div>
          </Card>
        </div>
      </PageHeaderWrapper>
    );
  }
}

export default TaskDetail;
