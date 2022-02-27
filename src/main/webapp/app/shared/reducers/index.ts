import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale from './locale';
import authentication from './authentication';
import applicationProfile from './application-profile';

import administration from 'app/modules/administration/administration.reducer';
import userManagement from 'app/modules/administration/user-management/user-management.reducer';
import register from 'app/modules/account/register/register.reducer';
import activate from 'app/modules/account/activate/activate.reducer';
import password from 'app/modules/account/password/password.reducer';
import settings from 'app/modules/account/settings/settings.reducer';
import passwordReset from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import country from 'app/entities/country/country.reducer';
// prettier-ignore
import uNrepresentative from 'app/entities/u-nrepresentative/u-nrepresentative.reducer';
// prettier-ignore
import car from 'app/entities/car/car.reducer';
// prettier-ignore
import enginier from 'app/entities/enginier/enginier.reducer';
// prettier-ignore
import student from 'app/entities/student/student.reducer';
// prettier-ignore
import course from 'app/entities/course/course.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  country,
  uNrepresentative,
  car,
  enginier,
  student,
  course,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
