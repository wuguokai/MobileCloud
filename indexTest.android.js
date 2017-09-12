import React, {
    Component,
} from 'react';

import {
    AppRegistry,
    View,
    Text
} from 'react-native';

export default class IndexTest extends Component {
  render() {
    return (
      <View>
          <Text>
          新的主模块!
          </Text>
      </View>
    )
  }
}

AppRegistry.registerComponent('MobileCloud', () => IndexTest);