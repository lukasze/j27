import { ICar } from 'app/shared/model/car.model';

export interface IEnginier {
  id?: number;
  fullName?: string | null;
  mobile?: string | null;
  car?: ICar | null;
}

export const defaultValue: Readonly<IEnginier> = {};
