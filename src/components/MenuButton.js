'use strict';
import React, {
    Component,
} from 'react';

import {
    View,
    Text,
    Image,
    TouchableWithoutFeedback,
    StyleSheet
} from 'react-native';

export default class MenuButton extends React.Component {

    static propTypes = {
        renderIcon: React.PropTypes.number.isRequired,
        showText: React.PropTypes.string,
        tag: React.PropTypes.string,  // Tag
        onClick: React.PropTypes.func,  // 回调函数
    };

    constructor(props) {
        super(props);
        this._onClick = this._onClick.bind(this);
    }

    _onClick() {
        if (this.props.onClick) {
            this.props.onClick(this.props.showText, this.props.tag);
        }
    }

    render() {
        return (
            <TouchableWithoutFeedback onPress={this._onClick}>
                <View style={{ alignItems: 'center', flex: 1 }}>
                    <Image style={styles.iconImg} source={this.props.renderIcon} />
                    <Text style={styles.showText}>{this.props.showText}</Text>
                </View>
            </TouchableWithoutFeedback>
        );
    }
}

const styles = StyleSheet.create({
    iconImg: {
        width: 38,
        height: 38,
        marginBottom: 2
    },
    showText: {
        fontSize: 12
    }
});