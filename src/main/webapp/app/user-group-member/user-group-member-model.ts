export class UserGroupMemberDTO {

  constructor(data:Partial<UserGroupMemberDTO>) {
    Object.assign(this, data);
  }

  id?: string|null;
  firstName?: string|null;
  lastName?: string|null;
  email?: string|null;
  preferredJDK?: string|null;
  usergroupId?: string|null;

}
