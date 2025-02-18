import { Resource } from 'app/common/hateoas';


export class UserGroupInfoDTO extends Resource  {

  constructor(data:Partial<UserGroupInfoDTO>) {
    super();
    Object.assign(this, data);
  }

  id?: string|null;
  groupID?: string|null;
  groupName?: string|null;
  isActive?: boolean|null;

}
