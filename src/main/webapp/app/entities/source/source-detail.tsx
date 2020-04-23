import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './source.reducer';
import { ISource } from 'app/shared/model/source.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISourceDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SourceDetail = (props: ISourceDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { sourceEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="jhipsterSampleApplicationApp.source.detail.title">Source</Translate> [<b>{sourceEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="sourceName">
              <Translate contentKey="jhipsterSampleApplicationApp.source.sourceName">Source Name</Translate>
            </span>
          </dt>
          <dd>{sourceEntity.sourceName}</dd>
          <dt>
            <span id="costUSD">
              <Translate contentKey="jhipsterSampleApplicationApp.source.costUSD">Cost USD</Translate>
            </span>
          </dt>
          <dd>{sourceEntity.costUSD}</dd>
          <dt>
            <span id="costARS">
              <Translate contentKey="jhipsterSampleApplicationApp.source.costARS">Cost ARS</Translate>
            </span>
          </dt>
          <dd>{sourceEntity.costARS}</dd>
          <dt>
            <span id="costDate">
              <Translate contentKey="jhipsterSampleApplicationApp.source.costDate">Cost Date</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={sourceEntity.costDate} type="date" format={APP_DATE_FORMAT} />
          </dd>
        </dl>
        <Button tag={Link} to="/source" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/source/${sourceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ source }: IRootState) => ({
  sourceEntity: source.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SourceDetail);
