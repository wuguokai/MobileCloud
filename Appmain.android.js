import React, { Component } from 'react';
import { AppRegistry, Text } from 'react-native';

export default class Module extends Component {
  render() {
    return (
      <Text>更新之后的Appmain！</Text>
    );
  }
}

// skip this line if using Create React Native App
AppRegistry.registerComponent('MobileCloud', () => Module);