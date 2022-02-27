import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './enginier.reducer';
import { IEnginier } from 'app/shared/model/enginier.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Enginier = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const enginierList = useAppSelector(state => state.enginier.entities);
  const loading = useAppSelector(state => state.enginier.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="enginier-heading" data-cy="EnginierHeading">
        <Translate contentKey="j27App.enginier.home.title">Enginiers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="j27App.enginier.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="j27App.enginier.home.createLabel">Create new Enginier</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {enginierList && enginierList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="j27App.enginier.id">Id</Translate>
                </th>
                <th>
                  <Translate contentKey="j27App.enginier.fullName">Full Name</Translate>
                </th>
                <th>
                  <Translate contentKey="j27App.enginier.mobile">Mobile</Translate>
                </th>
                <th>
                  <Translate contentKey="j27App.enginier.car">Car</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {enginierList.map((enginier, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${enginier.id}`} color="link" size="sm">
                      {enginier.id}
                    </Button>
                  </td>
                  <td>{enginier.fullName}</td>
                  <td>{enginier.mobile}</td>
                  <td>{enginier.car ? <Link to={`car/${enginier.car.id}`}>{enginier.car.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${enginier.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${enginier.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${enginier.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="j27App.enginier.home.notFound">No Enginiers found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Enginier;
