'use strict';
import React, {
    Component,
} from 'react';

import {
    StyleSheet,
    Image,
    Text,
    View,
    Navigator
} from 'react-native';

import Header from './Header';
import FirstPage from '../pages/FirstPage';
import SecondPage from '../pages/SecondPage';
import ThirdPage from '../pages/ThirdPage';
import TabNavigator from 'react-native-tab-navigator';

const FIRST = 'first';
const FIRST_NORMAL = require('../images/tabs/personal_normal.png');

const SECOND = 'second';
const SECOND_NORMAL = require('../images/tabs/personal_normal.png');

const THIRD = 'third';
const THIRD_NORMAL = require('../images/tabs/personal_normal.png');

const PERSONAL = 'personal';
const PERSONAL_NORMAL = require('../images/tabs/personal_normal.png');


export default class MainScreen extends Component {

    constructor(props) {
        super(props);
        this.state = { selectedTab: THIRD }
    }

    _renderTabItem(img, selectedImg, tag, childView, badgeText) {
        return (
            <TabNavigator.Item
                selected={this.state.selectedTab === tag}
                renderIcon={() => <Image style={styles.tabIcon} source={img} />}
                renderSelectedIcon={() => <Image style={styles.tabIconSelected} source={selectedImg} />}
                badgeText={badgeText}
                onPress={() => this.setState({ selectedTab: tag })}>
                {childView}
            </TabNavigator.Item>
        );
    }

    static _createChildView(tag) {
        return (
            <View style={{ flex: 1, backgroundColor: '#eee', alignItems: 'center', justifyContent: 'center' }}>
                <Text style={{ fontSize: 22 }}>{tag}</Text>
            </View>
        )
    }

    render() {
        return (
            <View style={{ flex: 1 }}>
                <Header />
                <TabNavigator hidesTabTouch={true} tabBarStyle={styles.tab}>
                    {this._renderTabItem(FIRST_NORMAL, FIRST_NORMAL, FIRST, <FirstPage />)}
                    {this._renderTabItem(SECOND_NORMAL, SECOND_NORMAL, SECOND, <SecondPage />)}
                    {this._renderTabItem(THIRD_NORMAL, THIRD_NORMAL, THIRD, <ThirdPage />)}
                    {this._renderTabItem(PERSONAL_NORMAL, PERSONAL_NORMAL, PERSONAL, MainScreen._createChildView(PERSONAL))}
                </TabNavigator>
            </View >
        );
    }
}

const styles = StyleSheet.create({
    tab: {
        height: 52,
        backgroundColor: '#303030',
        alignItems: 'center',
    },
    tabIcon: {
        width: 30,
        height: 35,
        resizeMode: 'stretch',
        marginTop: 12.5,
        tintColor: "white",
    },
    tabIconSelected: {
        width: 30,
        height: 35,
        resizeMode: 'stretch',
        marginTop: 12.5,
        tintColor: "lightskyblue",
    }
});