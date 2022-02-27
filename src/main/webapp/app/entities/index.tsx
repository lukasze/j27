import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Country from './country';
import UNrepresentative from './u-nrepresentative';
import Car from './car';
import Enginier from './enginier';
import Student from './student';
import Course from './course';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}country`} component={Country} />
      <ErrorBoundaryRoute path={`${match.url}u-nrepresentative`} component={UNrepresentative} />
      <ErrorBoundaryRoute path={`${match.url}car`} component={Car} />
      <ErrorBoundaryRoute path={`${match.url}enginier`} component={Enginier} />
      <ErrorBoundaryRoute path={`${match.url}student`} component={Student} />
      <ErrorBoundaryRoute path={`${match.url}course`} component={Course} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
