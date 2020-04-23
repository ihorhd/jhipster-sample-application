import { ISource } from 'app/shared/model/source.model';

export interface IProduct {
  id?: number;
  productName?: string;
  source?: ISource;
}

export const defaultValue: Readonly<IProduct> = {};
