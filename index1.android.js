/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */
import React, { Component } from 'react';
import { NativeModules } from 'react-native';
import {
  Alert,
  AppRegistry,
  StyleSheet,
  Text,
  View,
  TouchableHighlight,
  ToastAndroid
} from 'react-native';

export default class MobileCloud extends Component {
  constructor(props) {
    super(props);
    this.state = {}
  }
  //show(){
  //  NativeModules.NativeManager.show("hello",NativeModules.NativeManager.SHORT);
  //}

  press() {
    this.show("Appmain",1);
  }

  pressDown() {
    this.show("movie",2);
  }

  show(name, id) {
    NativeModules.NativeManager.openBundle(name, (type) => {
      let title = ""
      if (type == "update") {
        title = "发现新版本,是否升级?"
      } else {
        title = "未安装应用，是否安装？"
      }
      Alert.alert(
        title,
        title,
        [
          {
            text: '是',
            onPress: () => {
              NativeModules.NativeManager.downloadAndOpenBundle(name,id, (type, result) => {
                if (type == "success") {
                  ToastAndroid.show(result, ToastAndroid.SHORT);
                } else {
                  ToastAndroid.show(result, ToastAndroid.SHORT);
                }
              });
            }
          },
          {
            text: '否'
          }
        ]
      );
    });
  }


  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          安卓端主app
        </Text>
        <TouchableHighlight onPress={this.press.bind(this)}>
          <Text>子模块!</Text>
        </TouchableHighlight>
        <TouchableHighlight onPress={this.pressDown.bind(this)}>
          <Text>下载新模块</Text>
        </TouchableHighlight>
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


AppRegistry.registerComponent('MobileCloud', () => MobileCloud);