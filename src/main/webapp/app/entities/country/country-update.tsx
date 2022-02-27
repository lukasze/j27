import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUNrepresentative } from 'app/shared/model/u-nrepresentative.model';
import { getEntities as getUNrepresentatives } from 'app/entities/u-nrepresentative/u-nrepresentative.reducer';
import { getEntity, updateEntity, createEntity, reset } from './country.reducer';
import { ICountry } from 'app/shared/model/country.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CountryUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const uNrepresentatives = useAppSelector(state => state.uNrepresentative.entities);
  const countryEntity = useAppSelector(state => state.country.entity);
  const loading = useAppSelector(state => state.country.loading);
  const updating = useAppSelector(state => state.country.updating);
  const updateSuccess = useAppSelector(state => state.country.updateSuccess);
  const handleClose = () => {
    props.history.push('/country');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUNrepresentatives({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...countryEntity,
      ...values,
      unrepresentative: uNrepresentatives.find(it => it.id.toString() === values.unrepresentative.toString()),
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
          ...countryEntity,
          unrepresentative: countryEntity?.unrepresentative?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="j27App.country.home.createOrEditLabel" data-cy="CountryCreateUpdateHeading">
            <Translate contentKey="j27App.country.home.createOrEditLabel">Create or edit a Country</Translate>
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
                  id="country-id"
                  label={translate('j27App.country.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('j27App.country.name')} id="country-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label={translate('j27App.country.officialLanguage')}
                id="country-officialLanguage"
                name="officialLanguage"
                data-cy="officialLanguage"
                type="text"
              />
              <ValidatedField
                id="country-unrepresentative"
                name="unrepresentative"
                data-cy="unrepresentative"
                label={translate('j27App.country.unrepresentative')}
                type="select"
              >
                <option value="" key="0" />
                {uNrepresentatives
                  ? uNrepresentatives.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/country" replace color="info">
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

export default CountryUpdate;
