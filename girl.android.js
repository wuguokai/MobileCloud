import React, { Component } from 'react';
import { AppRegistry, Text } from 'react-native';

export default class Download extends Component {
  render() {
    return (
      <Text>girl更新之后的！</Text>
    );
  }
}

// skip this line if using Create React Native App
AppRegistry.registerComponent('MobileCloud', () => Download);