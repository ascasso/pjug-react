export class GroupMeetingDTO {

  constructor(data:Partial<GroupMeetingDTO>) {
    Object.assign(this, data);
  }

  id?: string|null;
  location?: string|null;
  meetingTopic?: string|null;
  meetingStartTime?: string|null;
  userGroupId?: string|null;

}
