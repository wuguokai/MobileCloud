'use strict';
import React, {
  Component,
} from 'react';

import {
  AppRegistry,
  Image,
  TextInput,
  View,
  Platform,
  StyleSheet
} from 'react-native';

import MainScreen from './components/MainScreen';

export default class Wrapper extends Component {
  render() {
    return (
      <MainScreen />
    )
  }
}

AppRegistry.registerComponent('MobileCloud', () => Wrapper);
