import React, {PureComponent} from 'react';
import {connect} from 'dva';
import {Button, Card, Form, Input, message} from 'antd';
import PageHeaderWrapper from '@/components/PageHeaderWrapper';
import ReactJson from 'react-json-view';
import { isJson } from '@/utils/utils';

const { TextArea } = Input;
const FormItem = Form.Item;

/* eslint react/no-multi-comp:0 */
@connect(({ rule }) => ({
  rule,
}))
@Form.create()
class RuleDebug extends PureComponent {
  state = {
    formValues: {},
  };

  componentDidMount() {
    const { request } = this.props.location.query;
    this.setState({
      formValues: { request },
    });
    const { dispatch } = this.props;
    dispatch({
      type: 'rule/clearDebug',
      payload: {},
    });
  }

  handleRequest = e => {
    e.preventDefault();
    const { form, dispatch } = this.props;
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      this.setState({
        formValues: {...fieldsValue},
      });
      const request = fieldsValue.request;
      dispatch({
        type: 'rule/sendRequest',
        payload: request,
      });
      message.success("发送请求成功");
    });
  };

  handleBuildRequest = e => {
    e.preventDefault();
    const { form } = this.props;
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      const request = fieldsValue.request;
      const response = fieldsValue.response;
      const type = fieldsValue.type;
      const ruleRequest = {type, data: {request, response}};
      const formValues = {...this.state.formValues, ruleRequest};
      this.setState({ formValues });
      message.success("生成规则请求成功");
    });
  };

  handleSendRuleRequest = e => {
    e.preventDefault();
    const { form, dispatch } = this.props;
    const { formValues: { ruleRequest } } = this.state;
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      const url = fieldsValue.ruleServer;
      dispatch({
        type: 'rule/sendRuleRequest',
        payload:{url, data: JSON.stringify(ruleRequest)},
      });
      message.success("发送请求成功");
    });
  };

  render() {
    const {
      rule: { response, ruleResponse },
      form: { getFieldDecorator },
    } = this.props;
    const { formValues } = this.state;

    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 7 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
        md: { span: 12 },
      },
    };

    const submitFormLayout = {
      wrapperCol: {
        xs: { span: 24, offset: 0 },
        sm: { span: 10, offset: 7 },
      },
    };

    return (
      <PageHeaderWrapper>
        <Card bordered={false}>
          <Form hideRequiredMark style={{ marginTop: 8 }}>
            <FormItem {...formItemLayout} label='请求'>
              {getFieldDecorator('request', {
                initialValue: formValues.request,
                rules: [{required: true, message: '请输入请求串！'}],
              })(
                <TextArea
                  style={{ minHeight: 32 }}
                  placeholder='请输入'
                  rows={6}
                />
              )}
            </FormItem>
            <FormItem {...submitFormLayout}>
              <Button type="primary" onClick={this.handleRequest}>发送请求</Button>
            </FormItem>
            <FormItem {...formItemLayout} label='请求结果'>
              {getFieldDecorator('response', {
                initialValue: response,
              })(
                <TextArea
                  style={{ minHeight: 32 }}
                  rows={6}
                />
              )}
            </FormItem>
            <FormItem {...formItemLayout} label='应用'>
              {getFieldDecorator('type')(<Input placeholder='请输入' />)}
            </FormItem>
            <FormItem {...submitFormLayout}>
              <Button type="primary" onClick={this.handleBuildRequest}>构建规则请求</Button>
            </FormItem>
            <FormItem {...formItemLayout} label='规则请求'>
              <ReactJson
                collapsed={2}
                displayDataTypes={false}
                src={formValues.ruleRequest}
                style={{minWidth: '300px'}}
                onEdit={
                  e => {
                    this.setState({ formValues: {...formValues, ruleRequest: e.updated_src}});
                  }
                }
                onDelete={
                  e => {
                    this.setState({ formValues: {...formValues, ruleRequest: e.updated_src}});
                  }
                }
                onAdd={
                  e => {
                    this.setState({ formValues: {...formValues, ruleRequest: e.updated_src}});
                  }
                }
              />
            </FormItem>
            <FormItem {...formItemLayout} label='规则引擎'>
              {getFieldDecorator('ruleServer', {
                initialValue: "http://127.0.0.1:9191/testruleengine/smoke",
              })(<Input placeholder='请输入' />)}
            </FormItem>
            <FormItem {...submitFormLayout}>
              <Button type="primary" onClick={this.handleSendRuleRequest}>规则验证</Button>
            </FormItem>
            <FormItem {...formItemLayout} label='验证结果'>
              {
                isJson(ruleResponse) ?
                  <ReactJson collapsed={2} src={JSON.parse(ruleResponse)} /> :
                  <TextArea
                    style={{ minHeight: 32 }}
                    rows={6}
                    value={ruleResponse}
                  />
              }
            </FormItem>
          </Form>
        </Card>
      </PageHeaderWrapper>
    );
  }
}

export default RuleDebug;
