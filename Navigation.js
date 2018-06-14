import React from 'react';

import { StackNavigator, DrawerNavigator } from 'react-navigation';

import PhotoOne from './screens/PhotoOne';
import PhotoTwo from './screens/PhotoTwo';
import Finger from './screens/Finger';

// const DrawerNavigation = DrawerNavigator(
//   {
//     Finger: { screen: Finger },
//     PhotoOne: { screen: PhotoOne },
//     PhotoTwo: { screen: PhotoTwo }
//   },
//   {
//     initialRouteName: "Finger",
//     contentOptions: {
//       activeTintColor: "#e91e63"
//     },
//     navigationOptions: {
//       headerStyle: {
//         backgroundColor: 'white',
//         borderColor: 'white',
//         justifyContent: 'center',
//         paddingTop: 13,
//         paddingBottom: 13,
//         height: 56,
//       },
//       headerTitleStyle: {
//         color: 'black',
//         textAlign: 'center',
//         alignSelf: 'center',
//       },
//       gesturesEnabled: false
//     }
//   }
// );

export default StackNavigator(
  {
    Finger: {
      screen: Finger
    },
    PhotoOne: {
      screen: PhotoOne
    },
    PhotoTwo: {
      screen: PhotoTwo
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