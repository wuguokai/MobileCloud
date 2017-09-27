import React, {
    Component,
} from 'react';

import {
    AppRegistry,
    Text,
    View,
    TouchableHighlight,
} from 'react-native';
import { StackNavigator } from 'react-navigation';

import MainScreen from './components/MainScreen';
import StackNavigatorSecond from './pages/demo/StackNavigatorSecond';
import StackNavigatorThird from './pages/demo/StackNavigatorThird'

export default MobileCloud = StackNavigator(
    {
        MainScreen: {
            screen: MainScreen,
            navigationOptions: ({ navigation }) => ({
                header: <View></View>,
            }),
        },
        StackNavigatorSecond: {
            screen: StackNavigatorSecond,
        },
        StackNavigatorThird: {
            screen: StackNavigatorThird,
        }
    },
    {
        navigationOptions: ({ navigation }) => ({
            headerRight:
            <View style={{ flexDirection: 'row' }}>
                <TouchableHighlight
                    onPress={() => alert("close")}
                >
                    <Text
                        style={{ fontSize: 12, color: '#FFFFFF', margin: 20, }}
                    >
                        返回
                </Text>
                </TouchableHighlight>
            </View>
            ,
            headerTintColor: '#FFFFFF',
            headerTitleStyle: ({
                fontSize: 12,
                alignSelf: 'center',
            }),
            headerStyle: ({
                backgroundColor: 'gray',
            }),
        }),
        mode: 'modal',
        headerMode: 'float',
    }
);

AppRegistry.registerComponent('MobileCloud', () => MobileCloud);