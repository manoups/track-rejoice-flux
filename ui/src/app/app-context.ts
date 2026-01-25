import {environment} from '../environments/environments';
import {AppCommonUtils} from './common/app-common-utils';
import {Role, UserProfile} from '@trackrejoice/typescriptmodels';

export class AppContext {
  static userProfile: UserProfile;
  static initials: string;

  static setUserProfile = (userProfile: UserProfile) => {
    if (!userProfile) {
      AppCommonUtils.clearCache();
    }
    this.userProfile = userProfile;
    this.initials = userProfile && (userProfile.details.firstName + ' ' + userProfile.details.lastName)
      .match(/(\b\S)?/g).join("").match(/(^\S|\S$)?/g).join("").toUpperCase();
  }

  static isAdmin = () => Role.ADMIN === this.userProfile.role;

  static isProduction = () => environment.production;

}
