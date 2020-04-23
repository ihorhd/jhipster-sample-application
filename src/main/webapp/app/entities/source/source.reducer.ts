import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISource, defaultValue } from 'app/shared/model/source.model';

export const ACTION_TYPES = {
  FETCH_SOURCE_LIST: 'source/FETCH_SOURCE_LIST',
  FETCH_SOURCE: 'source/FETCH_SOURCE',
  CREATE_SOURCE: 'source/CREATE_SOURCE',
  UPDATE_SOURCE: 'source/UPDATE_SOURCE',
  DELETE_SOURCE: 'source/DELETE_SOURCE',
  RESET: 'source/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISource>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type SourceState = Readonly<typeof initialState>;

// Reducer

export default (state: SourceState = initialState, action): SourceState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SOURCE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SOURCE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SOURCE):
    case REQUEST(ACTION_TYPES.UPDATE_SOURCE):
    case REQUEST(ACTION_TYPES.DELETE_SOURCE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SOURCE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SOURCE):
    case FAILURE(ACTION_TYPES.CREATE_SOURCE):
    case FAILURE(ACTION_TYPES.UPDATE_SOURCE):
    case FAILURE(ACTION_TYPES.DELETE_SOURCE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SOURCE_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_SOURCE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SOURCE):
    case SUCCESS(ACTION_TYPES.UPDATE_SOURCE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SOURCE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/sources';

// Actions

export const getEntities: ICrudGetAllAction<ISource> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SOURCE_LIST,
    payload: axios.get<ISource>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ISource> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SOURCE,
    payload: axios.get<ISource>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISource> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SOURCE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<ISource> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SOURCE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISource> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SOURCE,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
