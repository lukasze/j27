import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Enginier from './enginier';
import EnginierDetail from './enginier-detail';
import EnginierUpdate from './enginier-update';
import EnginierDeleteDialog from './enginier-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EnginierUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EnginierUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EnginierDetail} />
      <ErrorBoundaryRoute path={match.url} component={Enginier} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={EnginierDeleteDialog} />
  </>
);

export default Routes;
