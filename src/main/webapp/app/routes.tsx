import React from 'react';
import { createBrowserRouter, RouterProvider } from 'react-router';
import App from "./app";
import Home from './home/home';
import UserGroupInfoList from './user-group-info/user-group-info-list';
import UserGroupInfoAdd from './user-group-info/user-group-info-add';
import UserGroupInfoEdit from './user-group-info/user-group-info-edit';
import UserGroupMemberList from './user-group-member/user-group-member-list';
import UserGroupMemberAdd from './user-group-member/user-group-member-add';
import UserGroupMemberEdit from './user-group-member/user-group-member-edit';
import GroupMeetingList from './group-meeting/group-meeting-list';
import GroupMeetingAdd from './group-meeting/group-meeting-add';
import GroupMeetingEdit from './group-meeting/group-meeting-edit';
import Error from './error/error';
import { ROLE_USER } from 'app/security/authentication-provider';


export default function AppRoutes() {
  const router = createBrowserRouter([
    {
      element: <App />,
      children: [
        { path: '', element: <Home /> , handle: { roles: [ROLE_USER] } },
        { path: 'userGroupInfos', element: <UserGroupInfoList /> , handle: { roles: [ROLE_USER] } },
        { path: 'userGroupInfos/add', element: <UserGroupInfoAdd /> , handle: { roles: [ROLE_USER] } },
        { path: 'userGroupInfos/edit/:id', element: <UserGroupInfoEdit /> , handle: { roles: [ROLE_USER] } },
        { path: 'userGroupMembers', element: <UserGroupMemberList /> , handle: { roles: [ROLE_USER] } },
        { path: 'userGroupMembers/add', element: <UserGroupMemberAdd /> , handle: { roles: [ROLE_USER] } },
        { path: 'userGroupMembers/edit/:id', element: <UserGroupMemberEdit /> , handle: { roles: [ROLE_USER] } },
        { path: 'groupMeetings', element: <GroupMeetingList /> , handle: { roles: [ROLE_USER] } },
        { path: 'groupMeetings/add', element: <GroupMeetingAdd /> , handle: { roles: [ROLE_USER] } },
        { path: 'groupMeetings/edit/:id', element: <GroupMeetingEdit /> , handle: { roles: [ROLE_USER] } },
        { path: 'error', element: <Error /> },
        { path: '*', element: <Error /> }
      ]
    }
  ]);

  return (
    <RouterProvider router={router} />
  );
}
