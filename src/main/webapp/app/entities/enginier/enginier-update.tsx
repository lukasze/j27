import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ICar } from 'app/shared/model/car.model';
import { getEntities as getCars } from 'app/entities/car/car.reducer';
import { getEntity, updateEntity, createEntity, reset } from './enginier.reducer';
import { IEnginier } from 'app/shared/model/enginier.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const EnginierUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const cars = useAppSelector(state => state.car.entities);
  const enginierEntity = useAppSelector(state => state.enginier.entity);
  const loading = useAppSelector(state => state.enginier.loading);
  const updating = useAppSelector(state => state.enginier.updating);
  const updateSuccess = useAppSelector(state => state.enginier.updateSuccess);
  const handleClose = () => {
    props.history.push('/enginier');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCars({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...enginierEntity,
      ...values,
      car: cars.find(it => it.id.toString() === values.car.toString()),
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
          ...enginierEntity,
          car: enginierEntity?.car?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="j27App.enginier.home.createOrEditLabel" data-cy="EnginierCreateUpdateHeading">
            <Translate contentKey="j27App.enginier.home.createOrEditLabel">Create or edit a Enginier</Translate>
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
                  id="enginier-id"
                  label={translate('j27App.enginier.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('j27App.enginier.fullName')}
                id="enginier-fullName"
                name="fullName"
                data-cy="fullName"
                type="text"
              />
              <ValidatedField label={translate('j27App.enginier.mobile')} id="enginier-mobile" name="mobile" data-cy="mobile" type="text" />
              <ValidatedField id="enginier-car" name="car" data-cy="car" label={translate('j27App.enginier.car')} type="select">
                <option value="" key="0" />
                {cars
                  ? cars.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/enginier" replace color="info">
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

export default EnginierUpdate;
