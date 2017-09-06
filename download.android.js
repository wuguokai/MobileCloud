import React, { Component } from 'react';
import { AppRegistry, Text } from 'react-native';

export default class Download extends Component {
  render() {
    return (
      <Text>新下载的模块！</Text>
    );
  }
}

// skip this line if using Create React Native App
AppRegistry.registerComponent('MobileCloud', () => Download);