import React, {PureComponent} from 'react';
import SelectLang from '../SelectLang';
import styles from './index.less';

export default class GlobalHeaderRight extends PureComponent {

  render() {
    const {
      theme,
    } = this.props;
    let className = styles.right;
    if (theme === 'dark') {
      className = `${styles.right}  ${styles.dark}`;
    }
    return (
      <div className={className}>
        <SelectLang className={styles.action} />
      </div>
    );
  }
}
