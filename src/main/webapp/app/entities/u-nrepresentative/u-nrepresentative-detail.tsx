import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './u-nrepresentative.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UNrepresentativeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const uNrepresentativeEntity = useAppSelector(state => state.uNrepresentative.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="uNrepresentativeDetailsHeading">
          <Translate contentKey="j27App.uNrepresentative.detail.title">UNrepresentative</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="j27App.uNrepresentative.id">Id</Translate>
            </span>
          </dt>
          <dd>{uNrepresentativeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="j27App.uNrepresentative.name">Name</Translate>
            </span>
          </dt>
          <dd>{uNrepresentativeEntity.name}</dd>
          <dt>
            <span id="gender">
              <Translate contentKey="j27App.uNrepresentative.gender">Gender</Translate>
            </span>
          </dt>
          <dd>{uNrepresentativeEntity.gender}</dd>
        </dl>
        <Button tag={Link} to="/u-nrepresentative" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/u-nrepresentative/${uNrepresentativeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UNrepresentativeDetail;
