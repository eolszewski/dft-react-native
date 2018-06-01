import React from 'react';

import { StackNavigator } from 'react-navigation';

import Home from './screens/Home';
import Finger from './screens/Finger';

export default StackNavigator(
  {
    Home: {
      screen: Home
    },
    Finger: {
      screen: Finger
    }
  },
  {
    headerMode: "screen",
    mode: "card",
    navigationOptions: {
      headerStyle: {
        backgroundColor: 'white',
        borderColor: 'white',
        paddingTop: 13,
        paddingBottom: 13,
        height: 56,
      },
      headerTitleStyle: {
        color: 'black',
        textAlign: 'center',
        alignSelf: 'center',
      },
      gesturesEnabled: false
    }
  }
);