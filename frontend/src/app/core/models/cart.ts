import { Entry } from './entry';

export interface Cart {
  id: string;
  total: number;
  entries: Entry[];
}
