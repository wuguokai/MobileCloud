import React, { Component } from 'react';
import { AppRegistry, Text } from 'react-native';

export default class Module extends Component {
  render() {
    return (
      <Text>子模块！</Text>
    );
  }
}

// skip this line if using Create React Native App
AppRegistry.registerComponent('MobileCloud', () => Module);