import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ICountry } from 'app/shared/model/country.model';
import { getEntities as getCountries } from 'app/entities/country/country.reducer';
import { getEntity, updateEntity, createEntity, reset } from './u-nrepresentative.reducer';
import { IUNrepresentative } from 'app/shared/model/u-nrepresentative.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UNrepresentativeUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const countries = useAppSelector(state => state.country.entities);
  const uNrepresentativeEntity = useAppSelector(state => state.uNrepresentative.entity);
  const loading = useAppSelector(state => state.uNrepresentative.loading);
  const updating = useAppSelector(state => state.uNrepresentative.updating);
  const updateSuccess = useAppSelector(state => state.uNrepresentative.updateSuccess);
  const handleClose = () => {
    props.history.push('/u-nrepresentative');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCountries({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...uNrepresentativeEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...uNrepresentativeEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="j27App.uNrepresentative.home.createOrEditLabel" data-cy="UNrepresentativeCreateUpdateHeading">
            <Translate contentKey="j27App.uNrepresentative.home.createOrEditLabel">Create or edit a UNrepresentative</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="u-nrepresentative-id"
                  label={translate('j27App.uNrepresentative.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('j27App.uNrepresentative.name')}
                id="u-nrepresentative-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('j27App.uNrepresentative.gender')}
                id="u-nrepresentative-gender"
                name="gender"
                data-cy="gender"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/u-nrepresentative" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default UNrepresentativeUpdate;
