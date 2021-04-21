import {Product} from './product';

export interface Entry {
  id?: number;
  quantity: number;
  product?: Product;
  total?: number;
}
