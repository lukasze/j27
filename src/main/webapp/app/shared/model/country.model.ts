import { IUNrepresentative } from 'app/shared/model/u-nrepresentative.model';

export interface ICountry {
  id?: number;
  name?: string | null;
  officialLanguage?: string | null;
  unrepresentative?: IUNrepresentative | null;
}

export const defaultValue: Readonly<ICountry> = {};
