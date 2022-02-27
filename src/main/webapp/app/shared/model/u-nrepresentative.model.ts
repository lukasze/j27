import { ICountry } from 'app/shared/model/country.model';

export interface IUNrepresentative {
  id?: number;
  name?: string | null;
  gender?: string | null;
  country?: ICountry | null;
}

export const defaultValue: Readonly<IUNrepresentative> = {};
