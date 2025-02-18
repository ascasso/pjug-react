import { Resource } from 'app/common/hateoas';


export class UserGroupMemberDTO extends Resource  {

  constructor(data:Partial<UserGroupMemberDTO>) {
    super();
    Object.assign(this, data);
  }

  id?: string|null;
  firstName?: string|null;
  lastName?: string|null;
  email?: string|null;
  preferredJDK?: string|null;
  usergroupId?: string|null;

}
