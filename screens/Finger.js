import React from 'react';
import {
  StyleSheet, Text, View, NativeModules, NativeEventEmitter, Button
} from 'react-native';

class Finger extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      fingerprintData: null,
      params: null
    }
  }

  componentDidMount() {
    const eventEmitter = new NativeEventEmitter(NativeModules.Onyx);
    this.subscription = eventEmitter.addListener(NativeModules.Onyx.MyEventName, (params) => { 
      this.setState({ params });
      this.props.navigation.navigate('PhotoOne');
    });
  }

  componentWillUnmount() {
    this.subscription.remove();
  }

  render() {
    return (
      <View style={styles.container}>
        <Text>Click to launch the Onyx Demo from React Native</Text>
        <Button title="Demo" onPress={() => NativeModules.Onyx.navigateToOnyx()}>Onyx Demo</Button>
        <Button title="Get Fingerprint" onPress={
          async () => {
            try {
              var data = await NativeModules.Onyx.loadEnrolledTemplateIfExists();
              this.setState({ fingerprintData: data });
              console.log(data);
            } catch (e) {
              console.log(e.message);
            }
          }}>Get Fingerprint</Button>
        <Text numberOfLines={1}>{this.state.fingerprintData}</Text>
        <Button title="Load From State" onPress={
          () => {
            if (this.state.fingerprintData != null) {
              try {
                NativeModules.Onyx.loadEnrolledTemplateFromData(this.state.fingerprintData);
              } catch (e) {
                console.log(e.message);
              }
            }
          }}>Get Fingerprint</Button>
        <Text numberOfLines={1}>{this.state.params}</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});

export default Finger;