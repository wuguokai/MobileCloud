'use strict';
import React, {
    Component,
} from 'react';

import {
    View,
    Text,
    Image,
    StyleSheet,
    ScrollView,
    Alert,
    ListView,
    RefreshControl,
    Dimensions,
    PixelRatio,
    TouchableWithoutFeedback
} from 'react-native';
import MenuButton from '../components/MenuButton';

export default class FirstPage extends Component {

    _onMenuClick(title, tag) {
        Alert.alert('提示', '点击了:' + title + " Tag:" + tag);
    }

    render() {
        return (
            <View>
                <View style={styles.menuView}>
                    <MenuButton renderIcon={require('../images/home_icons/wdgz.png')}
                        showText={'模块1'} tag={'m1'}
                        onClick={this._onMenuClick} />
                    <MenuButton renderIcon={require('../images/home_icons/wlcx.png')}
                        showText={'模块2'} tag={'m2'}
                        onClick={this._onMenuClick} />
                    <MenuButton renderIcon={require('../images/home_icons/cz.png')}
                        showText={'模块3'} tag={'m3'}
                        onClick={this._onMenuClick} />
                    <MenuButton renderIcon={require('../images/home_icons/dyp.png')}
                        showText={'模块4'} tag={'m4'}
                        onClick={this._onMenuClick} />
                </View>
            </View>)
    }
}

const styles = StyleSheet.create({
    menuView: {
        flexDirection: 'row',
        marginTop: 10
    },
});