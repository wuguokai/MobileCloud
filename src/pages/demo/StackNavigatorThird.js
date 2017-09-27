'use strict';
import React, {
    Component,
} from 'react';
import { NativeModules } from 'react-native';
import {
    AppRegistry,
    Image,
    ListView,
    StyleSheet,
    Text,
    View,
    Alert,
    TouchableWithoutFeedback,
    RefreshControl,
    Dimensions,
    ToastAndroid,
    ScrollView,
} from 'react-native';
import Swiper from 'react-native-swiper';

var width = Dimensions.get('window').width;
export default class StackNavigatorThird extends Component {

    constructor(props) {
        super(props);

        this.state = {
            swiperShow: false,
            loaded: false,
        };
    }

    componentDidMount() {
        this.setState({
            loaded: true,
        });
        setTimeout(() => {
            this.setState({ swiperShow: true });
        }, 0)
    }
    renderSwiper = () => {
        if (this.state.swiperShow) {
            return (
                <Swiper style={styles.wrapper} height={150}
                    dot={<View style={{ backgroundColor: 'rgba(0,0,0,.2)', width: 5, height: 5, borderRadius: 4, marginLeft: 3, marginRight: 3, marginTop: 3, marginBottom: 3 }} />}
                    activeDot={<View style={{ backgroundColor: '#FFFFFF', width: 8, height: 8, borderRadius: 4, marginLeft: 3, marginRight: 3, marginTop: 3, marginBottom: 3 }} />}
                    paginationStyle={{
                        bottom: 10, right: 10, left: null,
                    }}
                    loop={true}
                    autoplay={false}
                    horizontal={true}
                    index={0}
                    autoplayTimeout={5}>
                    <View style={styles.slide} title={<Text numberOfLines={1}>Aussie tourist dies at Bali hotel</Text>}>
                        <Image resizeMode='stretch' style={styles.image} source={{ uri: 'http://img1.imgtn.bdimg.com/it/u=1781849339,3078928482&fm=200&gp=0.jpg' }} />
                    </View>
                    <View style={styles.slide} title={<Text numberOfLines={1}>Big lie behind Nine’s new show</Text>}>
                        <Image resizeMode='stretch' style={styles.image} source={{ uri: 'http://img1.imgtn.bdimg.com/it/u=1781849339,3078928482&fm=200&gp=0.jpg' }} />
                    </View>
                    <View style={styles.slide} title={<Text numberOfLines={1}>Why Stone split from Garfield</Text>}>
                        <Image resizeMode='stretch' style={styles.image} source={{ uri: 'http://img1.imgtn.bdimg.com/it/u=1781849339,3078928482&fm=200&gp=0.jpg' }} />
                    </View>
                </Swiper>
            );
        } else {
            return <View style={{ height: 150, }}></View>;
        }
    }

    render() {
        return (
            <View style={{ backgroundColor: '#F1F1F1' }}>
                <View style={{ height: 150, backgroundColor: '#FFFFFF' }}>
                    {this.renderSwiper()}
                </View>
            </View>
        );
    }
}

var styles = StyleSheet.create({
    containerLoading: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
    },
    container: {
        justifyContent: 'center',
        width: width / 4,
        height: 100,
        paddingTop: 20,
        alignItems: 'center',
        borderRightWidth: 1,
        //borderTopWidth:0.5,
        borderBottomWidth: 1,
        borderColor: '#EEEEEE',
    },
    rightContainer: {
        flex: 1,
    },
    headTitle: {
        backgroundColor: '#FFFFFF',
        borderLeftWidth: 5,
        borderLeftColor: '#0092DA',
        // height:20,
        marginTop: 15,
    },
    title: {
        textAlign: 'left',
        fontSize: 14,
        margin: 10,
        // marginLeft: 10,
    },
    year: {
        textAlign: 'center',
        fontSize: 12,
        marginTop: 3,
    },
    thumbnail: {
        width: 50,
        height: 50,
        borderRadius: 5,
    },
    listView: {
        //marginTop: 10,
        justifyContent: 'flex-start',
        flexDirection: 'row',
        flexWrap: 'wrap'
    },

    slide: {
        flex: 1,
        justifyContent: 'center',
        backgroundColor: 'transparent'
    },
    text: {
        color: '#fff',
        fontSize: 30,
        fontWeight: 'bold'
    },
    image: {
        width: width,
        flex: 1
    }

});