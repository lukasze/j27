import { IEnginier } from 'app/shared/model/enginier.model';

export interface ICar {
  id?: number;
  brand?: string | null;
  model?: string | null;
  enginiers?: IEnginier[] | null;
}

export const defaultValue: Readonly<ICar> = {};
