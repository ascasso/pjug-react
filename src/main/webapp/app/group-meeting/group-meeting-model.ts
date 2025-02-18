import { Resource } from 'app/common/hateoas';


export class GroupMeetingDTO extends Resource  {

  constructor(data:Partial<GroupMeetingDTO>) {
    super();
    Object.assign(this, data);
  }

  id?: string|null;
  location?: string|null;
  meetingTopic?: string|null;
  meetingStartTime?: string|null;
  userGroupId?: string|null;

}
