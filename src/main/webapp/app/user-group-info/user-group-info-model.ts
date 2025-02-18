export class UserGroupInfoDTO {

  constructor(data:Partial<UserGroupInfoDTO>) {
    Object.assign(this, data);
  }

  id?: string|null;
  groupID?: string|null;
  groupName?: string|null;
  isActive?: boolean|null;

}
