import React, {
    Component,
} from 'react';
import {
    Container,
    Header,
    Content,
    List,
    ListItem,
    Text,
    Icon,
    Left,
    Body,
    Right,
    Switch,
    Thumbnail
} from 'native-base';

export default class ListIconExample extends Component {

    static propTypes = {
        nav: React.PropTypes.object,
    };

    render() {
        const { navigate } = this.props.nav;
        return (
            <Container>
                <Content style={{ backgroundColor: '#F1F1F1', }}>
                    <List style={{ backgroundColor: '#FFFFFF', borderTopWidth: 0.5, borderTopColor: '#D1D1D1' }}>
                        <ListItem avatar style={{}}>
                            <Left>
                                <Thumbnail source={{ uri: 'https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2345970869,892410183&fm=173&s=8400FD17199351ED598CA8F503008063&w=218&h=146&img.JPEG' }} />
                            </Left>
                            <Body>
                                <Text>Huang Qi</Text>
                                <Text note>Doing what you like will always keep you happy . .</Text>
                            </Body>
                            <Right style={{ justifyContent: 'center', }}>
                                <Icon name="arrow-forward" />
                            </Right>
                        </ListItem>

                        <Content style={{ flex: 1, backgroundColor: '#eeeeee', height: 20 }}></Content>
                        <ListItem icon first>
                            <Left><Icon name="plane" style={{ color: 'lightskyblue' }} /></Left>
                            <Body><Text style={{ fontSize: 16 }}>订单信息</Text></Body>
                            <Right><Switch value={false} /></Right>
                        </ListItem>
                        <ListItem icon>
                            <Left><Icon name="wifi" style={{ color: '#fab614' }} /></Left>
                            <Body><Text style={{ fontSize: 16 }}>开票信息</Text></Body>
                            <Right><Text style={{ fontSize: 12 }}>正在开票中...</Text><Icon name="arrow-forward" /></Right>
                        </ListItem>
                        <ListItem icon last>
                            <Left><Icon name="bluetooth" style={{ color: '#F28B8B' }} /></Left>
                            <Body><Text style={{ fontSize: 16 }}>账号绑定</Text></Body>
                            <Right><Text style={{ fontSize: 12 }}>已绑定</Text><Icon name="arrow-forward" /></Right>
                        </ListItem>

                        <Content style={{ flex: 1, backgroundColor: '#eeeeee', height: 20 }}></Content>
                        <ListItem icon first>
                            <Left><Icon name="plane" style={{ color: 'lightskyblue' }} /></Left>
                            <Body><Text style={{ fontSize: 16 }}>在线客服</Text></Body>
                            <Right><Icon name="arrow-forward" /></Right>
                        </ListItem>
                        <ListItem icon>
                            <Left><Icon name="wifi" style={{ color: '#fab614' }} /></Left>
                            <Body><Text style={{ fontSize: 16 }}>帮助与反馈</Text></Body>
                            <Right><Icon name="arrow-forward" /></Right>
                        </ListItem>
                        <ListItem icon last>
                            <Left><Icon name="bluetooth" style={{ color: '#F28B8B' }} /></Left>
                            <Body><Text style={{ fontSize: 16 }}>关于</Text></Body>
                            <Right><Icon name="arrow-forward" /></Right>
                        </ListItem>

                        <Content style={{ flex: 1, backgroundColor: '#eeeeee', height: 20 }}></Content>
                        <ListItem icon first last>
                            <Left><Icon name="plane" /></Left>
                            <Body><Text style={{ fontSize: 16 }}>个人设置</Text></Body>
                            <Right><Icon name="arrow-forward" /></Right>
                        </ListItem>
                    </List>
                </Content>
            </Container>
        );
    }
}