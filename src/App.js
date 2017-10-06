import React, {
    Component,
} from 'react';

import {
    AppRegistry,
    Text,
    View,
    TouchableHighlight,
} from 'react-native';
import { StackNavigator, NavigationActions } from 'react-navigation';

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
const prevGetStateForAction = ReactNavigation.router.getStateForAction;
ReactNavigation.router.getStateForAction = (action, state) => {
    if (state && action.type === 'ReplaceCurrentScreen') {
        const routes = state.routes.slice(0, state.routes.length - 1);
        routes.push(action);
        return {
            ...state,
            routes,
            index: routes.length - 1,
        };
    }
    if (state && action.type === 'BcakToCurrentScreen') {
        function findDateInArr(arr, propertyName, value) {
            for (var i = 0; i < arr.length; i++) {
                if (arr[i][propertyName] == value) {
                    return i;
                }
            }
            return -1;
        }
        var i = findDateInArr(state.routes, 'routeName', action.routeName);
        if (i != -1) {
            var routes = state.routes.slice(0, i + 1);
            console.log('routes:-----');
            console.log(routes);
            console.log('action:-----');
            console.log(action);
        }
        return {
            ...state,
            routes,
            index: routes.length - 1,
        }
    }
    return prevGetStateForAction(action, state);
};

AppRegistry.registerComponent('MobileCloud', () => MobileCloud);