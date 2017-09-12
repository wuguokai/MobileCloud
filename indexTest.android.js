import React, {
    Component,
} from 'react';

import {
    AppRegistry,
    View,
} from 'react-native';

export default class IndexTest extends Component {
  render() {
    return (
      <view>
          新的主模块!
      </view>
    )
  }
}

AppRegistry.registerComponent('MobileCloud', () => IndexTest);