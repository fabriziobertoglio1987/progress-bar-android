'use strict';

import * as React from 'react';
import {ProgressBar} from '../js';

import RNTesterBlock from './RNTesterBlock';
import RNTesterPage from './RNTesterPage';

const MovingBar = (props) => {
  const [progress, setProgress] = React.useState(0);
  React.useEffect(() => {
    setInterval(() => {
      setProgress((p) => (p + 0.02) % 1);
    }, 50);
  }, []);
  return <ProgressBar progress={progress} {...props} />;
};

const App: React.ComponentType<{}> = () => {
  return (
    <RNTesterPage title="ProgressBar Examples">
      <RNTesterBlock title="Horizontal Indeterminate ProgressBar">
        <ProgressBar styleAttr="Horizontal" />
      </RNTesterBlock>
    </RNTesterPage>
  );
};

export default App;
