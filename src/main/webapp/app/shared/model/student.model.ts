import { ICourse } from 'app/shared/model/course.model';

export interface IStudent {
  id?: number;
  name?: string | null;
  courses?: ICourse[] | null;
}

export const defaultValue: Readonly<IStudent> = {};
