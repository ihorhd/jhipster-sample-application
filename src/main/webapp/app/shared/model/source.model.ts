import { Moment } from 'moment';
import { IProduct } from 'app/shared/model/product.model';

export interface ISource {
  id?: number;
  sourceName?: string;
  costUSD?: number;
  costARS?: number;
  costDate?: Moment;
  products?: IProduct[];
}

export const defaultValue: Readonly<ISource> = {};
