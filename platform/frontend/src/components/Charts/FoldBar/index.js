import React, {Component} from 'react';
import {Chart, Axis, Tooltip, Geom, Legend} from 'bizcharts';
import Debounce from 'lodash-decorators/debounce';
import Bind from 'lodash-decorators/bind';
import ResizeObserver from 'resize-observer-polyfill';
import styles from '../index.less';
import DataSet from "@antv/data-set";

class FoldBar extends Component {
  state = {
    height: 0,
    autoHideXLabels: false,
  };

  handleRoot = n => {
    this.root = n;
  };

  handleRef = n => {
    this.node = n;
  };

  resizeObserver() {
    const ro = new ResizeObserver(entries => {
      const {width, height} = entries[0].contentRect;
      this.resize();
      this.setState(preState => {
        if (preState.width !== width || preState.height !== height) {
          return {
            height,
          };
        }
        return null;
      });
    });
    if (this.root) {
      ro.observe(this.root);
    }
  }

  @Bind()
  @Debounce(400)
  resize() {
    if (!this.node) {
      return;
    }
    const canvasWidth = this.node.parentNode.clientWidth;
    const {data = [], autoLabel = true} = this.props;
    if (!autoLabel) {
      return;
    }
    const minWidth = data.length * 30;
    const {autoHideXLabels} = this.state;

    if (canvasWidth <= minWidth) {
      if (!autoHideXLabels) {
        this.setState({
          autoHideXLabels: true,
        });
      }
    } else if (autoHideXLabels) {
      this.setState({
        autoHideXLabels: false,
      });
    }
  }

  render() {
    const {
      height: propsHeight,
      title,
      forceFit = true,
      fields,
      data,
      padding,
    } = this.props;
    const ds = new DataSet();
    const dv = ds.createView().source(data);
    dv.transform({
      type: "fold",
      fields: fields,
      // 展开字段集
      key: "x",
      // key字段
      value: "y" // value字段
    });

    const {autoHideXLabels} = this.state;

    const scale = {
      x: {
        type: 'cat',
      },
      y: {
        min: 0,
      },
    };

    const {height: stateHeight} = this.state;
    const height = propsHeight || stateHeight;
    return (
      <div className={styles.chart} style={{height}} ref={this.handleRoot}>
        <div ref={this.handleRef}>
          {title && <h4 style={{marginBottom: 20}}>{title}</h4>}
          <Chart
            scale={scale}
            height={title ? height - 41 : height}
            forceFit={forceFit}
            data={dv}
            padding={padding || 'auto'}
          >
            <Legend/>
            <Axis name="x" title={false} label={autoHideXLabels ? false : {}} tickLine={autoHideXLabels ? false : {}}/>
            <Axis name="y" min={0}/>
            <Tooltip showTitle={false} crosshairs={false}/>
            <Geom
              type="intervalStack"
              position="x*y"
              color={"name"}
              style={{
                stroke: "#fff",
                lineWidth: 1
              }}
              size={30}
            />
          </Chart>
        </div>
      </div>
    );
  }
}

export default FoldBar;
