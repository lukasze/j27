import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UNrepresentative from './u-nrepresentative';
import UNrepresentativeDetail from './u-nrepresentative-detail';
import UNrepresentativeUpdate from './u-nrepresentative-update';
import UNrepresentativeDeleteDialog from './u-nrepresentative-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UNrepresentativeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UNrepresentativeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UNrepresentativeDetail} />
      <ErrorBoundaryRoute path={match.url} component={UNrepresentative} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UNrepresentativeDeleteDialog} />
  </>
);

export default Routes;
