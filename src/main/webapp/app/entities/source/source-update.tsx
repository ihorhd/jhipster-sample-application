import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './source.reducer';
import { ISource } from 'app/shared/model/source.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISourceUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SourceUpdate = (props: ISourceUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { sourceEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/source');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.costDate = convertDateTimeToServer(values.costDate);

    if (errors.length === 0) {
      const entity = {
        ...sourceEntity,
        ...values
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.source.home.createOrEditLabel">
            <Translate contentKey="jhipsterSampleApplicationApp.source.home.createOrEditLabel">Create or edit a Source</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : sourceEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="source-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="source-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="sourceNameLabel" for="source-sourceName">
                  <Translate contentKey="jhipsterSampleApplicationApp.source.sourceName">Source Name</Translate>
                </Label>
                <AvField id="source-sourceName" type="text" name="sourceName" />
              </AvGroup>
              <AvGroup>
                <Label id="costUSDLabel" for="source-costUSD">
                  <Translate contentKey="jhipsterSampleApplicationApp.source.costUSD">Cost USD</Translate>
                </Label>
                <AvField id="source-costUSD" type="string" className="form-control" name="costUSD" />
              </AvGroup>
              <AvGroup>
                <Label id="costARSLabel" for="source-costARS">
                  <Translate contentKey="jhipsterSampleApplicationApp.source.costARS">Cost ARS</Translate>
                </Label>
                <AvField id="source-costARS" type="string" className="form-control" name="costARS" />
              </AvGroup>
              <AvGroup>
                <Label id="costDateLabel" for="source-costDate">
                  <Translate contentKey="jhipsterSampleApplicationApp.source.costDate">Cost Date</Translate>
                </Label>
                <AvInput
                  id="source-costDate"
                  type="datetime-local"
                  className="form-control"
                  name="costDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.sourceEntity.costDate)}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/source" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  sourceEntity: storeState.source.entity,
  loading: storeState.source.loading,
  updating: storeState.source.updating,
  updateSuccess: storeState.source.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SourceUpdate);
