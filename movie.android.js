import React, { Component } from 'react';
import { AppRegistry, Text, View, Image, StyleSheet } from 'react-native';

export default class Download extends Component {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          movie
        </Text>
        <Text style={styles.instructions}>
          image
        </Text>
        <Image source={require('./img/test.png')} />
        <Text style={styles.instructions}>
          1.1.0
        </Text>
      </View>
    );
  }
}
const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
// skip this line if using Create React Native App
AppRegistry.registerComponent('MobileCloud', () => Download);