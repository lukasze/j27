import { IStudent } from 'app/shared/model/student.model';

export interface ICourse {
  id?: number;
  teacher?: string | null;
  students?: IStudent[] | null;
}

export const defaultValue: Readonly<ICourse> = {};
